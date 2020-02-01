import sys
import nltk
import pandas
import os
import warnings
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import AgglomerativeClustering


# Gestisce i path relativi in quanto lo script è chiamato dalla classe java
def relative_path(path):
    dirname = os.path.dirname(__file__)
    return os.path.join(dirname, path)


#  NOTA:
#  Il preprocessing è identico a quello nello script "preprocessing.py" è stato copiato per semplicità, in quanto la
#  gestione delle importazioni nel caso in cui lo script venga chiamato dalla classe java riultava molto complicato
#  in altro modo

def prepareStopWords():
    # nltk.download('stopwords') # la prima volta va scaricato
    # nltk.download('names')
    stopwords = nltk.corpus.stopwords.words('english')
    stopwords += nltk.corpus.names.words('male.txt') + nltk.corpus.names.words('female.txt')
    stopwords += ['although', 'seem', 'along', 'also', 'abov', 'afterward', 'alon', 'alreadi', 'alway', 'ani', 'anoth',
                  'anyon', 'anyth', 'anywher', 'becam',
                  'becaus', 'becom', 'befor', 'besid', 'cri', 'describ', 'dure', 'els', 'elsewher', 'empti', 'everi',
                  'everyon', 'everyth', 'everywher', 'fifti', 'forti', 'henc', 'hereaft', 'herebi', 'howev', 'hundr',
                  'inde', 'mani', 'meanwhil', 'moreov', 'nobodi', 'noon', 'noth', 'nowher', 'onc', 'onli', 'otherwis',
                  'ourselv', 'perhap', 'pleas', 'sever', 'sinc', 'sincer', 'sixti', 'someon', 'someth', 'sometim',
                  'somewher', 'themselv', 'thenc', 'thereaft', 'therebi', 'therefor', 'togeth', 'twelv', 'twenti',
                  'veri', 'whatev', 'whenc', 'whenev', 'wherea', 'whereaft', 'wherebi', 'wherev', 'whi', 'yourselv']
    stopwords += ['a.', "'d", "'s", 'dr.', 'mr.', 'anywh', 'could', 'doe', 'el', 'elsewh', 'everywh', 'ind', 'might',
                  'must', "n't", 'need', 'otherwi', 'plea', 'sha', 'somewh', 'wo', 'would']
    return map(lambda x: x.lower(), stopwords)


def tokenize_and_stem(text):
    # nltk.download('punkt')  # la prima volta va scaricato
    stemmer = nltk.SnowballStemmer("english")
    tokens = [word for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)]
    filtered_tokens = []
    for token in tokens:
        if nltk.re.search('[a-zA-Z]', token):
            filtered_tokens.append(token)
    stems = [stemmer.stem(t) for t in filtered_tokens]
    return stems


def preprocessing(dataset, min_df=0.1, max_df=0.9, max_features=None):
    plots = dataset.__getattr__("Plot")

    stopwords = prepareStopWords()

    tfidf_vectorizer = TfidfVectorizer(min_df=min_df, max_df=max_df, max_features=max_features,
                                       stop_words=stopwords,
                                       use_idf=True, tokenizer=tokenize_and_stem, ngram_range=(1, 3))
    tfidf_matrix = tfidf_vectorizer.fit_transform(plots)  # esegue la vettorizzazzione
    tfidf_vector = tfidf_matrix.toarray()
    terms = tfidf_vectorizer.get_feature_names()  # lista dei termini presi in considerazione

    result_dataset = dataset
    result_dataset.drop('Plot', axis=1, inplace=True)

    for j in range(0, len(terms)):
        values = []

        for i in range(0, len(tfidf_vector)):
            values.insert(len(values), tfidf_vector[i][j])

        result_dataset[terms[j]] = values

    return result_dataset


if __name__ == '__main__':
    # Causano problemi con la classe java
    warnings.filterwarnings("ignore")

    # Viene ripulito il file contenete i risultati precedenti
    resultFile = open(relative_path("../resources/elaborations/clustering_results.txt"), "w+")
    resultFile.write("")
    resultFile.close()

    # Disabilita un warning che è stato appurato apparire come falso positivo
    pandas.options.mode.chained_assignment = None

    # Numero di campioni per cluster circa passato dalla classe java alla chiamata
    samples_in_cluster = int(sys.argv[1])
    # Vengono prelevati i film da clusterizzare
    dataset = pandas.read_csv(relative_path("../resources/datasets/to_be_clusterized.csv"), encoding='latin1')
    # Viene eseguito il preprocessing tf-idf
    data = preprocessing(dataset=dataset, min_df=0.03, max_df=0.82, max_features=1196)

    # Si prelevano soltanto i valori delle features
    X = data.iloc[:, 1:].values

    # Si esegue il clustering gerarchico agglomerativo
    clustering_model = AgglomerativeClustering(n_clusters=round(len(data.index) / samples_in_cluster),
                                               affinity='euclidean', linkage='complete')
    clustering_model.fit_predict(X)

    # Viene inserita la colonna relativa ai cluster di assegnazione dei vari film
    data.insert(0, "Cluster", clustering_model.labels_)

    # Viene apreto il file per il salvataggio dei risultati
    resultFile = open(relative_path("../resources/elaborations/clustering_results.txt"), "a+")

    # Per ogni film viene salvato il cluster di appartenenza
    for cluster_id in clustering_model.labels_:
        resultFile.write("\n" + str(cluster_id))

    # Per ogni cluster viene calcolato il baricentro e salvato il termine più importante e quelli con peso non
    # inferiore a metà
    # I termini dei vari cluster sono separati dal simbolo "$"
    for c in range(0, clustering_model.n_clusters):
        cluster = data[data["Cluster"] == c]
        cluster.drop("Cluster", axis=1, inplace=True)
        terms_weight = cluster.mean()
        for item in terms_weight.items():
            if item[1] > max(terms_weight) * 0.5:
                resultFile.write("\n" + item[0])
        resultFile.write("\n$")

    resultFile.close()
