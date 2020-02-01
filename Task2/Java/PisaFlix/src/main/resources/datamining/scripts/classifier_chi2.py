import nltk
import pandas
import os
import warnings
from sklearn.ensemble import RandomForestClassifier
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.preprocessing import normalize, scale


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


# Prevede di calcolare il valore standardizzato dei film da classificare utilizzando lo stesso vocabolario dei film
# utilizzati per calcolare il modello (tramite la feature selection chi2)
def preprocessing(trained_data):
    # Vengono prelevati i film da classificare
    to_be_classified = pandas.read_csv(relative_path("../resources/datasets/to_be_classified.csv"))
    # Vengono prelevati i termini del vocabolario
    terms = list(trained_data)[2:]

    # Vengono preparate le stopwords
    stopwords = prepareStopWords()

    # Viene eseguita la vettorizzazione e il conteggio delle parole del vocabolario
    vectorizer = CountVectorizer(stop_words=stopwords, tokenizer=tokenize_and_stem, ngram_range=(1, 3),
                                 vocabulary=terms)
    word_count_matrix = vectorizer.fit_transform(to_be_classified.__getattr__("Plot"))

    # Creazine di un dataframe dalla matrice sparsa
    result_dataset = pandas.SparseDataFrame(word_count_matrix, columns=vectorizer.get_feature_names())
    # Rimozione dei nan e unione delle features calcolate con quelle precedenti
    result_dataset = pandas.concat([to_be_classified, result_dataset.fillna(0)], axis=1)
    # Unione dei dataset
    result_dataset.append(trained_data, ignore_index=True)
    # Standardizzazione dei valori presenti nel dataset (deve essere effettuata solo nelle features dei termini)
    tmp = pandas.DataFrame(scale(result_dataset.iloc[:, 2:]), columns=vectorizer.get_feature_names())
    # Riunione di tutte le features
    result_dataset = pandas.concat([result_dataset.iloc[:, 0:1], tmp], axis=1)
    # Unione del dataset per il modello con quello da classificare
    result_dataset = trained_data.append(result_dataset, ignore_index=True)
    return result_dataset


if __name__ == '__main__':
    warnings.filterwarnings("ignore")

    # Prelievo dei film del modello
    dataset = pandas.read_csv(relative_path("../resources/datasets/preprocessedData_chi2.csv"))
    dataset.drop('Title', axis=1, inplace=True)
    dataset.drop('Year', axis=1, inplace=True)

    # Preprocessing
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
    model = RandomForestClassifier(criterion="entropy").fit(X, y)

    # I risultati vengono stampati sullo standard output
    for row in model.predict_proba(C):
        print(str(row[0]))
