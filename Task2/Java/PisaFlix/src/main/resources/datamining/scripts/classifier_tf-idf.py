import nltk
import pandas
import os
import warnings
from sklearn.ensemble import RandomForestClassifier
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer, TfidfTransformer


# Gestisce i path relativi in quanto lo script è chiamato dalla classe java
def relative_path(path):
    dirname = os.path.dirname(__file__)
    return os.path.join(dirname, path)


# Preparazione delle stopwords
def prepareStopWords():
    # nltk.download('stopwords') # la prima volta va scaricato
    # nltk.download('names')
    stopwords = nltk.corpus.stopwords.words('english')
    stopwords += nltk.corpus.names.words('male.txt') + nltk.corpus.names.words('female.txt')
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
    return map(lambda x: x.lower(), stopwords)


# Tokenization e steam del testo passato
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


# Prevede di calcolare il valore tf-idf dei film da classificare utilizzando lo stesso vocabolario dei film utilizzati
# per calcolare il modello
def preprocessing(trained_data):
    # Vengono prelevati i film che devono essere classificati
    to_be_classified = pandas.read_csv(relative_path("../resources/datasets/to_be_classified.csv"))
    # Le prime due features non sono termini da aggiungere al vocabolario
    terms = list(trained_data)[2:]

    # Preparazione delle stopwords
    stopwords = prepareStopWords()

    # Semplice vettorizzazione e conta delle apparizioni dei termini presenti nel vocabolario nei film da classificare
    vectorizer = CountVectorizer(stop_words=stopwords, tokenizer=tokenize_and_stem, ngram_range=(1, 3),
                                 vocabulary=terms)
    word_count_matrix = vectorizer.fit_transform(to_be_classified.__getattr__("Plot"))
    # La matrice sparsa del risultato va trasformata in un dataframe
    result_dataset = pandas.SparseDataFrame(word_count_matrix, columns=vectorizer.get_feature_names())
    # Vanno tolti i nan
    result_dataset = result_dataset.fillna(0)
    # E' eseguita la normalizzazione tf-idf
    result_dataset = TfidfTransformer().fit_transform(result_dataset)
    # La matrice sparsa del risultato va trasformata in un dataframe
    result_dataset = pandas.SparseDataFrame(result_dataset, columns=vectorizer.get_feature_names())
    # Vanno tolti i nan
    result_dataset = result_dataset.fillna(0)
    # Le nuove features vengono aggiunte a quelle iniziali
    result_dataset = pandas.concat([to_be_classified, result_dataset], axis=1)
    # Viene creato un un unico dataframe contenente i film di training e quelli da classificare
    result_dataset = trained_data.append(result_dataset, ignore_index=True)
    return result_dataset


if __name__ == '__main__':

    warnings.filterwarnings("ignore")

    # Si prelevano i film per la creazione del modello e del vocabolario
    dataset = pandas.read_csv(relative_path("../resources/datasets/preprocessedData_tf-idf.csv"))
    # Si eliminano le features superflue
    dataset.drop('Title', axis=1, inplace=True)
    dataset.drop('Year', axis=1, inplace=True)
    # Si esegue il preprocessing
    data = preprocessing(trained_data=dataset)
    # Superflua
    data.drop('Plot', axis=1, inplace=True)

    # Si dividono i film del modello da quelli da classificare
    class_ADULTS = data[data["MPAA"] == "ADULTS"]
    class_CHILDREN = data[data["MPAA"] == "CHILDREN"]
    model_tuples = class_ADULTS.append(class_CHILDREN, ignore_index=True)
    to_be_classified_tuples = data[data["MPAA"] == "to_be_classified"]

    # Si trasformano i dati nel modello necessario alla classificazione
    X = model_tuples.iloc[:, 1:].values
    y = model_tuples['MPAA']
    C = to_be_classified_tuples.iloc[:, 1:].values

    # Si effettua la classificazione con un classificatore random forest con gini index
    model = RandomForestClassifier(criterion="entropy").fit(X, y)  # (1)

    # I risultati vengono stampati sullo standard output
    for row in model.predict_proba(C):
        print(str(row[0]))

    # NOTA:
    #   (1) Il modello potrebbe essere calcolato solo la prima volta e salvato per esempio con un "pickling", dato che
    #       dopo i relativi test è stato deciso di utilizzare il metodo in "clasifier.py" non è stato ritenuto
    #       necessario implementarlo.
