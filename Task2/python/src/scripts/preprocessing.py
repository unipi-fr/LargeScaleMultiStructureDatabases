from array import array

import pandas
import nltk
import re
from nltk.stem.snowball import SnowballStemmer
from numpy import size
from sklearn.feature_extraction.text import TfidfVectorizer


# Parole non utili per il clustering
def prepareStopWords():
    #nltk.download('stopwords') # la prima volta va scaricato
    #nltk.download('names')
    stopwords = nltk.corpus.stopwords.words('english')
    stopwords += ['abov', 'afterward', 'alon', 'alreadi', 'alway', 'ani', 'anoth', 'anyon', 'anyth', 'anywher', 'becam',
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
    #nltk.download('punkt')  # la prima volta va scaricato
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


if __name__ == '__main__':
    dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")
    stopwords = prepareStopWords()

    # Vettorizzazzione dei plot utilizzando la "term frequency–inverse document frequency"
    tfidf_vectorizer = TfidfVectorizer(max_df=0.4, max_features=100,  # (1)
                                       min_df=0.01, stop_words=stopwords,
                                       use_idf=True, tokenizer=tokenize_and_stem, ngram_range=(1, 3))
    tfidf_matrix = tfidf_vectorizer.fit_transform(dataset.__getattr__("Plot")) #esegue la vettorizzazzione
    tfidf_vector = tfidf_matrix.toarray()
    terms = tfidf_vectorizer.get_feature_names() #lista dei termini presi in considerazione
    resultDataset = dataset
    print(terms)

    for j in range(0, len(terms)):
        values = []

        for i in range(0, len(tfidf_vector)):
            values.insert(len(values), tfidf_vector[i][j])

        resultDataset[terms[j]] = values

    print(resultDataset)
    resultDataset.to_csv("../resources/elaborations/vectorizedData.csv", index=False)

    # NOTES:
#    (1) For uncorrelated features, the optimal feature size is N−1 (where N is sample size)
#        As feature correlation increases, and the optimal feature size becomes proportional to √N for highly correlated features.



