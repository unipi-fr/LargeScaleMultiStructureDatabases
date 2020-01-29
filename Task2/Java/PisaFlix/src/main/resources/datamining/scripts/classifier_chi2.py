import nltk
import pandas
import os
import warnings
from sklearn.ensemble import RandomForestClassifier
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.preprocessing import normalize, scale


def relative_path(path):
    dirname = os.path.dirname(__file__)
    return os.path.join(dirname, path)


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


def preprocessing(trained_data):
    to_be_classified = pandas.read_csv(relative_path("../resources/datasets/to_be_classified.csv"))
    terms = list(trained_data)[2:]

    stopwords = prepareStopWords()

    vectorizer = CountVectorizer(stop_words=stopwords, tokenizer=tokenize_and_stem, ngram_range=(1, 3),
                                 vocabulary=terms)
    word_count_matrix = vectorizer.fit_transform(to_be_classified.__getattr__("Plot"))
    result_dataset = pandas.SparseDataFrame(word_count_matrix, columns=vectorizer.get_feature_names())
    result_dataset = pandas.concat([to_be_classified, result_dataset.fillna(0)], axis=1)
    result_dataset.append(trained_data, ignore_index=True)
    tmp = pandas.DataFrame(scale(result_dataset.iloc[:, 2:]), columns=vectorizer.get_feature_names())
    result_dataset = pandas.concat([result_dataset.iloc[:, 0:1], tmp], axis=1)
    result_dataset = trained_data.append(result_dataset, ignore_index=True)
    return result_dataset


if __name__ == '__main__':

    warnings.filterwarnings("ignore")

    dataset = pandas.read_csv(relative_path("../resources/datasets/trainedData.csv"))
    dataset.drop('Title', axis=1, inplace=True)
    dataset.drop('Year', axis=1, inplace=True)

    data = preprocessing(trained_data=dataset)
    data.drop('Plot', axis=1, inplace=True)

    class_ADULTS = data[data["MPAA"] == "ADULTS"]
    class_CHILDREN = data[data["MPAA"] == "CHILDREN"]
    model_tuples = class_ADULTS.append(class_CHILDREN, ignore_index=True)
    to_be_classified_tuples = data[data["MPAA"] == "to_be_classified"]

    X = model_tuples.iloc[:, 1:].values
    y = model_tuples['MPAA']
    C = to_be_classified_tuples.iloc[:, 1:].values

    model = RandomForestClassifier(criterion="entropy").fit(X, y)

    for row in model.predict_proba(C):
        print(str(row[0]))
