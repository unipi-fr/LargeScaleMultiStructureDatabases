import pandas
from joblib import load
import os

def relativePath(path):
    dirname = os.path.dirname(__file__)
    return os.path.join(dirname, path)    

if __name__ == '__main__':

    data = pandas.read_csv(relativePath('../resources/elaborations/vectorizedData.csv'))
    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    model = load(relativePath('../resources/elaborations/trained_model.joblib'))
    to_be_classified = [X[len(X) - 1]]
    print(str(model.predict_proba(to_be_classified)[0][0]))
