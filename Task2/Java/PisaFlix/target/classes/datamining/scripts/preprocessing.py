import os
from array import array
import pandas
import nltk
import re
from nltk.stem.snowball import SnowballStemmer
from numpy import size
from sklearn.feature_extraction.text import TfidfVectorizer


def relativePath(path):
    dirname = os.path.dirname(__file__)
    return os.path.join(dirname, path)


# Parole non utili per il clustering
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


# Uno "stemmer" permette di ricavare le "radici" delle parole, cat <- cats, catlike, catty
# Un "tokenizer" divide un testo nelle parole che lo compongono, questa funzione effettua anche lo "steam" ed elimina le parole superflue

def tokenize_and_stem(text):
    # nltk.download('punkt')  # la prima volta va scaricato
    stemmer = SnowballStemmer("english")
    # first tokenize by sentence, then by word to ensure that punctuation is caught as it's own token
    tokens = [word for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)]
    filtered_tokens = []
    # filter out any tokens not containing letters (e.g., numeric tokens, raw punctuation) o stopwords
    for token in tokens:
        if re.search('[a-zA-Z]', token):
            filtered_tokens.append(token)
    stems = [stemmer.stem(t) for t in filtered_tokens]
    return stems


def classifier_preprocessiong(dataset, min_df=0.1, max_df=0.9, max_features=None):
    # Sampling dei dati con rimpiazzo in base alla classe
    class_ADULTS = dataset[dataset["MPAA"] == "ADULTS"]
    class_CHILDREN = dataset[dataset["MPAA"] == "CHILDREN"]
    # class_TEENAGERS = dataset[dataset["MPAA"] == "TEENAGERS"]

    # class_ADULTS = class_ADULTS.sample(700)
    # class_CHILDREN = class_CHILDREN.sample(700)
    # class_TEENAGERS = class_TEENAGERS.sample(700)

    sampled_dataset = class_ADULTS.append(class_CHILDREN, ignore_index=True)
    # sampled_dataset = sampled_dataset.append(class_TEENAGERS, ignore_index=True)

    stopwords = prepareStopWords()

    # Vettorizzazzione dei plot utilizzando la "term frequency–inverse document frequency"
    tfidf_vectorizer = TfidfVectorizer(min_df=min_df, max_df=max_df, max_features=max_features,
                                       stop_words=stopwords,
                                       use_idf=True, tokenizer=tokenize_and_stem, ngram_range=(1, 3))
    tfidf_matrix = tfidf_vectorizer.fit_transform(sampled_dataset.__getattr__("Plot"))  # esegue la vettorizzazzione
    tfidf_vector = tfidf_matrix.toarray()
    terms = tfidf_vectorizer.get_feature_names()  # lista dei termini presi in considerazione
    result_dataset = sampled_dataset
    # print(terms)

    for j in range(0, len(terms)):
        values = []

        for i in range(0, len(tfidf_vector)):
            values.insert(len(values), tfidf_vector[i][j])

        result_dataset[terms[j]] = values

    # print(result_dataset)
    return result_dataset


def preprocessing(dataset, min_df=0.1, max_df=0.9, max_features=None):

    dataset.drop('Title', axis=1, inplace=True)
    dataset.drop('Year', axis=1, inplace=True)
    class_ADULTS = dataset[dataset["MPAA"] == "ADULTS"]
    class_CHILDREN = dataset[dataset["MPAA"] == "CHILDREN"]
    dataset = class_ADULTS.append(class_CHILDREN, ignore_index=True)

    tbc_file = open(relativePath("../resources/datasets/to_be_classified.txt"), "r")
    tmp = {"MPAA": ["to_be_classified"], "Plot": [tbc_file.read()]}
    tbc_file.close()
    to_be_classified = pandas.DataFrame(tmp)
    dataset = dataset.append(to_be_classified, ignore_index=True)

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
    dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")
    result_dataset = preprocessing(dataset=dataset, min_df=0.1, max_df=0.9, max_features=None)
    result_dataset.to_csv("../resources/elaborations/vectorizedData.csv", index=False)
