from numpy import mean
from sklearn.linear_model import LogisticRegression, RidgeClassifier
from sklearn.metrics import confusion_matrix, accuracy_score, make_scorer, classification_report
from sklearn.model_selection import cross_val_score
from sklearn.neighbors import KNeighborsClassifier
from sklearn import svm
import pandas
from sklearn.tree import DecisionTreeClassifier
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis, QuadraticDiscriminantAnalysis
from sklearn.naive_bayes import GaussianNB
from scipy.optimize import differential_evolution
from Java.PisaFlix.src.main.resources.datamining.scripts.preprocessing import classifier_preprocessiong
from joblib import dump, load

# GLOBAL VARIABLES
dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")
pop = [0, 1, 0]
pop_size = 15


def classification_report_with_accuracy_score(y_true, y_pred):
    # print(confusion_matrix(y_true, y_pred))
    # print(classification_report(y_true, y_pred))
    return accuracy_score(y_true, y_pred)


def classification(x):
    global pop
    global dataset
    global pop_size

    min_df = x[0]
    max_df = x[1]
    max_features = int(round(x[2]))

    pop[0] += 1

    if pop[0] == pop_size + 1:
        pop[0] = 1
        print("\n--- End of generation: " + str(pop[1]))
        print("         BEST ACC: " + str(pop[2]) + "\n")

        log = open("../resources/elaborations/Convergenza2.txt", "a+")
        log.write(
            "\n" + "\n--- End of generation: " + str(pop[1]) + "\n" + "         BEST ACC: " + str(pop[2]) + "\n\n")
        log.close()

        pop[2] = 0
        pop[1] += 1

    print("- Pop: " + str(pop[0]) + " Generation: " + str(pop[1]))

    log = open("../resources/elaborations/Convergenza2.txt", "a+")
    log.write("\n" + "- Pop: " + str(pop[0]) + " Generation: " + str(pop[1]))
    log.close()

    if min_df > max_df:
        print("         Inconsistent parameters")

        log = open("../resources/elaborations/Convergenza2.txt", "a+")
        log.write("\n" + "          Inconsistent parameters")
        log.close()

        return 2.0

    try:
        data = classifier_preprocessiong(dataset=dataset, min_df=min_df, max_df=max_df, max_features=max_features)
    except:

        log = open("../resources/elaborations/Convergenza2.txt", "a+")
        log.write("\n" + "          No terms for those parameters")
        log.close()

        print("         No terms for those parameters")
        return 2.0

    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    LR_model = LogisticRegression()

    CV_LR = []
    mean_acc = 0
    try:
        for i in range(1, 10):
            CV_LR.append(cross_val_score(LR_model, X, y, cv=10,
                                         scoring=make_scorer(classification_report_with_accuracy_score)))
        mean_acc = mean(CV_LR)
    except:
        print("         Error in the regression")

        log = open("../resources/elaborations/Convergenza2.txt", "a+")
        log.write("\n" + "          Error in the regression")
        log.close()

        return 2.0

    print("         Arguments: min_df=" + str(min_df) + ", max_df=" + str(max_df) +
          ", max_features=" + str(max_features) + "\n           RESULT: " + str(mean_acc))

    log = open("../resources/elaborations/Convergenza2.txt", "a+")
    log.write("\n" + "          Arguments: min_df=" + str(min_df) + ", max_df=" + str(max_df) +
              ", max_features=" + str(max_features) + "\n           RESULT: " + str(mean_acc))
    log.close()

    log = open("../resources/elaborations/Convergenza2.csv", "a+")
    log.write("\n" + str(min_df) + "," + str(max_df) + "," + str(max_features) + "," + str(mean_acc))
    log.close()

    if pop[2] < mean_acc:
        pop[2] = mean_acc

    return 1.0 - mean_acc


if __name__ == '__main__':
    # data = pandas.read_csv("../resources/elaborations/vectorizedData.csv")
    # data.drop('Title', axis=1, inplace=True)
    # data.drop('Year', axis=1, inplace=True)
    # data.drop('Plot', axis=1, inplace=True)
    # # # print(data)
    # #
    # X = data.iloc[:, 1:-1].values
    # y = data['MPAA']
    # #
    # # LR_model = LogisticRegression()
    # # trained_model = LR_model.fit(X, y)
    # # # print(trained_model.score(X, y))
    # # dump(trained_model, '../resources/elaborations/trained_model.joblib')
    #
    # model = load('../resources/elaborations/trained_model.joblib')
    # to_be_classified = [X[len(X) - 1]]
    # print(to_be_classified)
    # print(y[len(X) - 1])
    # print(model.predict_proba(to_be_classified))

    # # (1) K-Nearest Neighbors
    # KNN_model = KNeighborsClassifier(n_neighbors=5)
    # CV_KNN = cross_val_score(KNN_model, X, y, cv=10)
    # print(mean(CV_KNN))
    #
    # # (2) Decision Tree Classifier
    # DT_model = DecisionTreeClassifier(1, )
    # CV_DT = cross_val_score(DT_model, X, y, cv=10)
    # print(mean(CV_DT))
    #
    # (3) Support Vector Machines
    # SVC_model = svm.SVC()
    # CV_SVC = cross_val_score(SVC_model, X, y, cv=10)
    # print(mean(CV_SVC))

    # (4) Logistic Regression
    # LR_model = LogisticRegression()
    # CV_LR = cross_val_score(LR_model, X, y, cv=10, scoring=make_scorer(classification_report_with_accuracy_score))
    # print(mean(CV_LR))

    #
    # # (5) Discriminant Analysis
    # # Linear
    # LD_model = LinearDiscriminantAnalysis()
    # CV_LD = cross_val_score(LD_model, X, y, cv=10)
    # print(mean(CV_LD))
    # # Quadratic
    # QD_model = QuadraticDiscriminantAnalysis()
    # CV_QD = cross_val_score(QD_model, X, y, cv=10)
    # print(mean(CV_QD))  # Problema di collinearità!

    # (6) Gaussian Naive Bayesian
    # GNB_model = GaussianNB()
    # CV_GNB = cross_val_score(GNB_model, X, y, cv=10)
    # print(mean(CV_GNB))

    log = open("../resources/elaborations/Convergenza2.csv", "w+")
    log.write("min_df,max_df,max_features,accuracy")
    log.close()

    log = open("../resources/elaborations/Convergenza2.txt", "w+")
    log.write("DIFFERENTIAL EVOLUTION TEXT LOG\n")
    log.close()

    bounds = [(0, 1), (0, 1), (38, 1400)]
    differential_evolution(classification, bounds, popsize=pop_size)

# NOTES:
#   (1): K-Nearest Neighbors operates by checking the distance from some test example to the known values of some
#   training example. The group of data points/class that would give the smallest distance between the training points
#   and the testing point is the class that is selected.
#
#   (2): A Decision Tree Classifier functions by breaking down a dataset into smaller and smaller subsets based on
#   different criteria. Different sorting criteria will be used to divide the dataset, with the number of examples
#   getting smaller with every division.
#   Once the network has divided the data down to one example, the example will be put into a class that corresponds to
#   a key. When multiple random forest classifiers are linked together they are called Random Forest Classifiers.
#
#   (3): Support Vector Machines work by drawing a line between the different clusters of data points to group them
#   into classes. Points on one side of the line will be one class and points on the other side belong to another class.
#   The classifier will try to maximize the distance between the line it draws and the points on either side of it, to
#   increase its confidence in which points belong to which class. When the testing points are plotted, the side of the
#   line they fall on is the class they are put in.
#
#   (4): Logistic Regression outputs predictions about test data points on a binary scale, zero or one. If the value of
#   something is 0.5 or above, it is classified as belonging to class 1, while below 0.5 if is classified as belonging
#   to 0.
#   Each of the features also has a label of only 0 or 1. Logistic regression is a linear classifier and therefore used
#   when there is some sort of linear relationship between the data.
