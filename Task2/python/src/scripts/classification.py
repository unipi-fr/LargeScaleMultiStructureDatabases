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


def classification_report_with_accuracy_score(y_true, y_pred):
    print(confusion_matrix(y_true, y_pred))
    print(classification_report(y_true, y_pred))
    return accuracy_score(y_true, y_pred)


if __name__ == '__main__':

    data = pandas.read_csv("../resources/elaborations/vectorizedData.csv")
    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)
    data.to_csv("../resources/elaborations/data.csv", index=False)
    print(data)

    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    # # (1) K-Nearest Neighbors
    # KNN_model = KNeighborsClassifier(n_neighbors=5)
    # CV_KNN = cross_val_score(KNN_model, X, y, cv=10)
    # print(mean(CV_KNN))
    #
    # # (2) Decision Tree Classifier
    # DT_model = DecisionTreeClassifier(random_state=0)
    # CV_DT = cross_val_score(DT_model, X, y, cv=10)
    # print(mean(CV_DT))
    #
    # # (3) Support Vector Machines
    # SVC_model = svm.SVC()
    # CV_SVC = cross_val_score(SVC_model, X, y, cv=10)
    # print(mean(CV_SVC))

    #(4) Logistic Regression
    LR_model = LogisticRegression()
    CV_LR = cross_val_score(LR_model, X, y, cv=10, scoring=make_scorer(classification_report_with_accuracy_score))
    print(mean(CV_LR))

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
    #GNB_model = GaussianNB()
    #CV_GNB = cross_val_score(GNB_model, X, y, cv=10)
    #print(mean(CV_GNB))

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




