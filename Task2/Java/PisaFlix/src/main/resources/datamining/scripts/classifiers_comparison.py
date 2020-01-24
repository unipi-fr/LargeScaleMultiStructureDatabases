import warnings
import pandas
from numpy import mean
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, roc_auc_score
from sklearn.model_selection import StratifiedKFold
from sklearn.naive_bayes import GaussianNB
from sklearn.discriminant_analysis import QuadraticDiscriminantAnalysis
from sklearn.ensemble import RandomForestClassifier, AdaBoostClassifier

if __name__ == '__main__':
    warnings.filterwarnings("ignore")
    data = pandas.read_csv("../resources/datasets/preprocessedData.csv")
    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    acc_LR = []
    auc_LR = []
    acc_NG = []
    auc_NG = []
    acc_QD = []
    auc_QD = []
    acc_RF = []
    auc_RF = []

    SKF = StratifiedKFold(n_splits=10, random_state=12345, shuffle=True)
    for train_index, test_index in SKF.split(X, y):
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]

        y_test_num = []
        for i in y_test:
            if i == 'ADULTS':
                y_test_num.insert(-1, 1)
            else:
                y_test_num.insert(-1, 0)

        # Regression
        LR_model = LogisticRegression().fit(X_train, y_train)
        y_predicted = LR_model.predict(X_test)
        y_score = [row[0] for row in LR_model.predict_proba(X_test)]

        acc_LR.insert(-1, accuracy_score(y_test, y_predicted))
        auc_LR.insert(-1, roc_auc_score(y_test_num, y_score))

        # Bayesian
        NG_model = GaussianNB().fit(X_train, y_train)
        y_predicted = NG_model.predict(X_test)
        y_score = [row[0] for row in NG_model.predict_proba(X_test)]

        acc_NG.insert(-1, accuracy_score(y_test, y_predicted))
        auc_NG.insert(-1, roc_auc_score(y_test_num, y_score))

        # Discriminant
        QD_model = QuadraticDiscriminantAnalysis().fit(X_train, y_train)
        y_predicted = QD_model.predict(X_test)
        y_score = [row[0] for row in QD_model.predict_proba(X_test)]

        acc_QD.insert(-1, accuracy_score(y_test, y_predicted))
        auc_QD.insert(-1, roc_auc_score(y_test_num, y_score))

        # Random Forest
        RF_model = RandomForestClassifier(random_state=12345).fit(X_train, y_train)
        y_predicted = RF_model.predict(X_test)
        y_score = [row[0] for row in RF_model.predict_proba(X_test)]  # (1)

        acc_RF.insert(-1, accuracy_score(y_test, y_predicted))
        auc_RF.insert(-1, roc_auc_score(y_test_num, y_score))

    print()
    print("Linear Regression:")
    print("- Mean accuracy: " + str(round(mean(acc_LR) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_LR) - max(acc_LR)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_LR) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_LR) - max(auc_LR)) * 100, 1)) + "%")
    print()
    print("Gaussian Naive Bayesian:")
    print("- Mean accuracy: " + str(round(mean(acc_NG) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_NG) - max(acc_NG)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_NG) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_NG) - max(auc_NG)) * 100, 1)) + "%")
    print()
    print("Quadratic Discriminant:")
    print("- Mean accuracy: " + str(round(mean(acc_QD) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_QD) - max(acc_QD)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_QD) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_QD) - max(auc_QD)) * 100, 1)) + "%")
    print()
    print("Random Forest:")
    print("- Mean accuracy: " + str(round(mean(acc_RF) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_RF) - max(acc_RF)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_RF) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_RF) - max(auc_RF)) * 100, 1)) + "%")

    # NOTES:
    #   (1) The predicted class probabilities of an input sample in random forest are computed as the mean predicted
    #   class probabilities of the trees in the forest. The class probability of a single tree is the fraction of
    #   samples of the same class in a leaf.

# todo mettere la stratified kfold nell'evoluzione
