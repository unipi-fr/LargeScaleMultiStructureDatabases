import sys
import nltk
import pandas
import os
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import AgglomerativeClustering


def relative_path(path):
    dirname = os.path.dirname(__file__)
    return os.path.join(dirname, path)


def prepareStopWords():
    # nltk.download('stopwords') # la prima volta va scaricato
    # nltk.download('names')
    stopwords = nltk.corpus.stopwords.words('english')
    stopwords += ['although', 'along', 'also', 'abov', 'afterward', 'alon', 'alreadi', 'alway', 'ani', 'anoth', 'anyon',
                  'anyth', 'anywher', 'becam',
                  'becaus', 'becom', 'befor', 'besid', 'cri', 'describ', 'dure', 'els', 'elsewher', 'empti', 'everi',
                  'everyon', 'everyth', 'everywher', 'fifti', 'forti', 'henc', 'hereaft', 'herebi', 'howev', 'hundr',
                  'inde', 'mani', 'meanwhil', 'moreov', 'nobodi', 'noon', 'noth', 'nowher', 'onc', 'onli', 'otherwis',
                  'ourselv', 'perhap', 'pleas', 'sever', 'sinc', 'sincer', 'sixti', 'someon', 'someth', 'sometim',
                  'somewher', 'themselv', 'thenc', 'thereaft', 'therebi', 'therefor', 'togeth', 'twelv', 'twenti',
                  'veri', 'whatev', 'whenc', 'whenev', 'wherea', 'whereaft', 'wherebi', 'wherev', 'whi', 'yourselv']
    stopwords += ['a.', "'d", "'s", 'anywh', 'could', 'doe', 'el', 'elsewh', 'everywh', 'ind', 'might', 'must', "n't",
                  'need', 'otherwi', 'plea', 'sha', 'somewh', 'wo', 'would']
    return stopwords


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

    # print(result_dataset)
    return result_dataset


if __name__ == '__main__':
    samples_in_cluster = int(sys.argv[1])  # Numero di campioni per cluster circa
    dataset = pandas.read_csv(relative_path("../resources/datasets/to_be_clusterized.csv"), encoding='latin1')
    data = preprocessing(dataset=dataset, min_df=0.04, max_df=0.74, max_features=1300)

    X = data.iloc[:, 1:-1].values
    clustering_model = AgglomerativeClustering(n_clusters=round(len(data.index)/samples_in_cluster), affinity='euclidean',
                                               linkage='ward')
    clustering_model.fit_predict(X)

    data.insert(0, "Cluster", clustering_model.labels_)

    for cluster_id in clustering_model.labels_:
        print(str(cluster_id))

    for c in range(0, clustering_model.n_clusters - 1):
        cluster = data[data["Cluster"] == c]
        cluster.drop("Cluster", axis=1, inplace=True)
        terms_weight = cluster.mean()
        for item in terms_weight.items():
            if item[1] > max(terms_weight) * 0.5:
                print(item[0])
        print("$")
