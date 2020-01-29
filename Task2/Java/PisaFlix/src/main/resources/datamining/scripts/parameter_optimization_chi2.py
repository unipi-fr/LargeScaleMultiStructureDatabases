import warnings
import pandas
from numpy import mean
from scipy.stats import chi2
from sklearn.ensemble import RandomForestClassifier, AdaBoostClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score
from sklearn.model_selection import StratifiedKFold
from scipy.optimize import minimize, brute
from sklearn.feature_selection import chi2
from Java.PisaFlix.src.main.resources.datamining.scripts.preprocessing import select_k_best_preprocessing

# GLOBAL PARAMETERS
dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")


def classification(n_terms):
    global dataset

    max_features = int(round(n_terms[0]))

    data = select_k_best_preprocessing(dataset, chi2, max_features)

    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    random_forest = RandomForestClassifier(criterion="entropy")

    CV_ACC = []
    try:
        for i in range(1, 10):
            SKF = StratifiedKFold(n_splits=10, shuffle=True)
            for train_index, test_index in SKF.split(X, y):
                X_train, X_test = X[train_index], X[test_index]
                y_train, y_test = y[train_index], y[test_index]
                model = random_forest.fit(X_train, y_train)
                y_predicted = model.predict(X_test)
                CV_ACC.insert(len(CV_ACC), accuracy_score(y_test, y_predicted))
        mean_acc = mean(CV_ACC)
    except:
        print("Error in classification")
        return 2.0

    print(str(max_features) + " " + str(mean_acc))

    log = open("../resources/elaborations/optimization_log.csv", "a+")
    log.write("\n" + str(max_features) + "," + str(mean_acc))
    log.close()

    return 1.0 - mean_acc


if __name__ == '__main__':
    warnings.filterwarnings("ignore")

    log = open("../resources/elaborations/optimization_log.csv", "w+")
    log.write("terms,accuracy")
    log.close()

    bounds = [(38, 1400)]
    brute(classification, bounds, workers=-1)
