import os
import pandas
from joblib import load

if __name__ == '__main__':
    print("1")
    data = pandas.read_csv("../resources/elaborations/vectorizedData.csv")
    print("2")
    data.drop('Title', axis=1, inplace=True)
    print("3")
    data.drop('Year', axis=1, inplace=True)
    print("4")
    data.drop('Plot', axis=1, inplace=True)
    print("5")

    X = data.iloc[:, 1:-1].values
    print("6")
    y = data['MPAA']
    print("7")

    model = load('../resources/elaborations/trained_model.joblib')
    print("8")
    to_be_classified = [X[len(X) - 1]]
    print("9")
    print(str(model.predict_proba(to_be_classified)[0][0]))
