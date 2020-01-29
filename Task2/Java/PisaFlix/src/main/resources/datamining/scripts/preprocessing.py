import os
import time
import warnings
import pandas
import nltk
import re
from nltk.stem.snowball import SnowballStemmer
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.feature_selection import mutual_info_classif, SelectKBest, chi2
from sklearn.preprocessing import normalize


# Parole non utili per il clustering
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


# Uno "stemmer" permette di ricavare le "radici" delle parole, cat <- cats, catlike, catty
# Un "tokenizer" divide un testo nelle parole che lo compongono, questa funzione effettua anche lo "steam" ed elimina
# le parole superflue
def tokenize_and_stem(text):
    # nltk.download('punkt')  # la prima volta va scaricato
    stemmer = SnowballStemmer("english")
    tokens = [word for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)]
    filtered_tokens = []
    for token in tokens:
        if re.search('[a-zA-Z]', token):
            filtered_tokens.append(token)
    stems = [stemmer.stem(t) for t in filtered_tokens]
    return stems


def tf_idf_preprocessing(dataset, min_df=0.1, max_df=0.9, max_features=None):
    class_ADULTS = dataset[dataset["MPAA"] == "ADULTS"]
    class_CHILDREN = dataset[dataset["MPAA"] == "CHILDREN"]
    dataset = class_ADULTS.append(class_CHILDREN, ignore_index=True)

    stopwords = prepareStopWords()

    # Vettorizzazzione dei plot utilizzando la "term frequency–inverse document frequency"
    tfidf_vectorizer = TfidfVectorizer(min_df=min_df, max_df=max_df, max_features=max_features,
                                       stop_words=stopwords,
                                       use_idf=True, tokenizer=tokenize_and_stem, ngram_range=(1, 3))
    tfidf_matrix = tfidf_vectorizer.fit_transform(dataset.__getattr__("Plot"))  # esegue la vettorizzazzione
    tfidf_vector = tfidf_matrix.toarray()
    terms = tfidf_vectorizer.get_feature_names()  # lista dei termini presi in considerazione
    result_dataset = dataset

    for j in range(0, len(terms)):
        values = []

        for i in range(0, len(tfidf_vector)):
            values.insert(len(values), tfidf_vector[i][j])

        result_dataset[terms[j]] = values

    return result_dataset


def select_k_best_preprocessing(dataset, method, n_features, vocabulary=None):
    class_ADULTS = dataset[dataset["MPAA"] == "ADULTS"]
    class_CHILDREN = dataset[dataset["MPAA"] == "CHILDREN"]
    dataset = class_ADULTS.append(class_CHILDREN, ignore_index=True)
    y = dataset['MPAA']

    stopwords = prepareStopWords()

    vectorizer = CountVectorizer(stop_words=stopwords, tokenizer=tokenize_and_stem, ngram_range=(1, 3),
                                 vocabulary=vocabulary)
    word_count_matrix = vectorizer.fit_transform(dataset.__getattr__("Plot"))
    terms = vectorizer.get_feature_names()

    select_k = SelectKBest(method, k=n_features)
    selected_features = select_k.fit_transform(word_count_matrix, y)
    mask = select_k.get_support()

    selected_terms = []
    for bool, feature in zip(mask, terms):
        if bool:
            selected_terms.append(feature)

    result_dataset = pandas.SparseDataFrame(selected_features, columns=selected_terms)
    result_dataset = pandas.DataFrame(normalize(result_dataset.fillna(0)), columns=selected_terms)
    result_dataset = pandas.concat([dataset, result_dataset], axis=1)

    return result_dataset


if __name__ == '__main__':
    warnings.filterwarnings("ignore")
    # start_time = time.time()
    raw_dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")

    # data = tf_idf_preprocessing(dataset=raw_dataset, min_df=0.1, max_df=0.9, max_features=500)
    # data = select_k_best_preprocessing(raw_dataset, chi2, 500)
    # data = select_k_best_preprocessing(raw_dataset, mutual_info_classif, 500)
    # print("Execution time: " + str(time.time() - start_time))
    # data.to_csv("../resources/datasets/preprocessedData.csv", index=False)
    data = select_k_best_preprocessing(raw_dataset, chi2, 1385)
    data.to_csv("../resources/datasets/trainedData.csv", index=False)
