import warnings
import pandas
from numpy import mean
from sklearn.tree import DecisionTreeClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, roc_auc_score
from sklearn.model_selection import StratifiedKFold
from sklearn.naive_bayes import GaussianNB
from sklearn.svm import SVC
from sklearn.neighbors import KNeighborsClassifier
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
    acc_RF_Gini = []
    auc_RF_Gini = []
    acc_RF_Entropy = []
    auc_RF_Entropy = []
    acc_SV = []
    auc_SV = []
    acc_DT = []
    auc_DT = []
    acc_KNN = []
    auc_KNN = []
    acc_ADA_LR = []
    auc_ADA_LR = []
    acc_ADA_DT = []
    auc_ADA_DT = []

    SKF = StratifiedKFold(n_splits=10, random_state=12345, shuffle=True)
    for train_index, test_index in SKF.split(X, y):
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]

        y_test_num = []
        for i in y_test:
            if i == 'ADULTS':
                y_test_num.insert(len(y_test_num), 1)
            else:
                y_test_num.insert(len(y_test_num), 0)

        # Regression
        LR_model = LogisticRegression().fit(X_train, y_train)
        y_predicted = LR_model.predict(X_test)
        y_score = [row[0] for row in LR_model.predict_proba(X_test)]

        acc_LR.insert(len(acc_LR), accuracy_score(y_test, y_predicted))
        auc_LR.insert(len(auc_LR), roc_auc_score(y_test_num, y_score))

        # Bayesian
        NG_model = GaussianNB().fit(X_train, y_train)
        y_predicted = NG_model.predict(X_test)
        y_score = [row[0] for row in NG_model.predict_proba(X_test)]

        acc_NG.insert(len(acc_NG), accuracy_score(y_test, y_predicted))
        auc_NG.insert(len(auc_NG), roc_auc_score(y_test_num, y_score))

        # Support Vectors
        SV_model = SVC(probability=True, random_state=12345).fit(X_train, y_train)
        y_predicted = SV_model.predict(X_test)
        y_score = [row[0] for row in SV_model.predict_proba(X_test)]

        acc_SV.insert(len(acc_SV), accuracy_score(y_test, y_predicted))
        auc_SV.insert(len(auc_SV), roc_auc_score(y_test_num, y_score))

        # Decision Tree
        DT_model = DecisionTreeClassifier(criterion="gini", random_state=12345).fit(X_train, y_train)
        y_predicted = DT_model.predict(X_test)
        y_score = [row[0] for row in DT_model.predict_proba(X_test)]

        acc_DT.insert(len(acc_DT), accuracy_score(y_test, y_predicted))
        auc_DT.insert(len(auc_DT), roc_auc_score(y_test_num, y_score))

        # KNeighbors
        KNN_model = KNeighborsClassifier().fit(X_train, y_train)
        y_predicted = KNN_model.predict(X_test)
        y_score = [row[0] for row in KNN_model.predict_proba(X_test)]

        acc_KNN.insert(len(acc_KNN), accuracy_score(y_test, y_predicted))
        auc_KNN.insert(len(auc_KNN), roc_auc_score(y_test_num, y_score))

        # Discriminant
        QD_model = QuadraticDiscriminantAnalysis().fit(X_train, y_train)
        y_predicted = QD_model.predict(X_test)
        y_score = [row[0] for row in QD_model.predict_proba(X_test)]

        acc_QD.insert(len(acc_QD), accuracy_score(y_test, y_predicted))
        auc_QD.insert(len(auc_QD), roc_auc_score(y_test_num, y_score))

        # Random Forest Gini
        RF_model = RandomForestClassifier(random_state=12345, criterion="gini").fit(X_train, y_train)
        y_predicted = RF_model.predict(X_test)
        y_score = [row[0] for row in RF_model.predict_proba(X_test)]  # (1)

        acc_RF_Gini.insert(len(acc_RF_Gini), accuracy_score(y_test, y_predicted))
        auc_RF_Gini.insert(len(auc_RF_Gini), roc_auc_score(y_test_num, y_score))

        # Random Forest Entropy
        RF_model = RandomForestClassifier(random_state=12345, criterion="entropy").fit(X_train, y_train)
        y_predicted = RF_model.predict(X_test)
        y_score = [row[0] for row in RF_model.predict_proba(X_test)]  # (1)

        acc_RF_Entropy.insert(len(acc_RF_Entropy), accuracy_score(y_test, y_predicted))
        auc_RF_Entropy.insert(len(auc_RF_Entropy), roc_auc_score(y_test_num, y_score))

        # ADAboost Regression
        ADA_LR_model = AdaBoostClassifier(random_state=12345, base_estimator=LogisticRegression(), n_estimators=100)\
            .fit(X_train, y_train)
        y_predicted = ADA_LR_model.predict(X_test)
        y_score = [row[0] for row in ADA_LR_model.predict_proba(X_test)]  # (2)

        acc_ADA_LR.insert(len(acc_ADA_LR), accuracy_score(y_test, y_predicted))
        auc_ADA_LR.insert(len(auc_ADA_LR), roc_auc_score(y_test_num, y_score))

        # ADAboost Forest
        ADA_DT_model = AdaBoostClassifier(random_state=12345, base_estimator=DecisionTreeClassifier(
            criterion="gini", random_state=12345), n_estimators=100).fit(X_train, y_train)
        y_predicted = ADA_DT_model.predict(X_test)
        y_score = [row[0] for row in ADA_DT_model.predict_proba(X_test)]  # (1-2)

        acc_ADA_DT.insert(len(acc_ADA_DT), accuracy_score(y_test, y_predicted))
        auc_ADA_DT.insert(len(auc_ADA_DT), roc_auc_score(y_test_num, y_score))

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
    print("Decision Tree:")
    print("- Mean accuracy: " + str(round(mean(acc_DT) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_DT) - max(acc_DT)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_DT) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_DT) - max(auc_DT)) * 100, 1)) + "%")
    print()
    print("Support Vectors:")
    print("- Mean accuracy: " + str(round(mean(acc_SV) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_SV) - max(acc_SV)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_SV) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_SV) - max(auc_SV)) * 100, 1)) + "%")
    print()
    print("K-nearest Neighbors:")
    print("- Mean accuracy: " + str(round(mean(acc_KNN) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_KNN) - max(acc_KNN)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_KNN) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_KNN) - max(auc_KNN)) * 100, 1)) + "%")
    print()
    print("Random Forest with Gini Index:")
    print("- Mean accuracy: " + str(round(mean(acc_RF_Gini) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_RF_Gini) - max(acc_RF_Gini)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_RF_Gini) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_RF_Gini) - max(auc_RF_Gini)) * 100, 1)) + "%")
    print()
    print("Random Forest with Entropy:")
    print("- Mean accuracy: " + str(round(mean(acc_RF_Entropy) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_RF_Entropy) - max(acc_RF_Entropy)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_RF_Entropy) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_RF_Entropy) - max(auc_RF_Entropy)) * 100, 1)) + "%")
    print()
    print("ADAboost of Linear Regressions:")
    print("- Mean accuracy: " + str(round(mean(acc_ADA_LR) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_ADA_LR) - max(acc_ADA_LR)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_ADA_LR) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_ADA_LR) - max(auc_ADA_LR)) * 100, 1)) + "%")
    print()
    print("ADAboost of Decision Trees:")
    print("- Mean accuracy: " + str(round(mean(acc_ADA_DT) * 100, 1)) + "% ± " + str(
        round(abs(mean(acc_ADA_DT) - max(acc_ADA_DT)) * 100, 1)) + "%")
    print("- Mean AUC: " + str(round(mean(auc_ADA_DT) * 100, 1)) + "% ± " + str(
        round(abs(mean(auc_ADA_DT) - max(auc_ADA_DT)) * 100, 1)) + "%")

    # NOTES:
    #   (1) The predicted class probabilities of an input sample in random forest are computed as the mean predicted
    #   class probabilities of the trees in the forest. The class probability of a single tree is the fraction of
    #   samples of the same class in a leaf.
    #
    #   (2) The predicted class probabilities of an input sample is computed as the weighted mean predicted class
    #   probabilities of the classifiers in the ensemble
