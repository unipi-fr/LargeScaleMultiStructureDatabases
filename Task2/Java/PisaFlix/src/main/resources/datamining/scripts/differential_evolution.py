import warnings
from numpy import mean
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, make_scorer
from sklearn.model_selection import cross_val_score, StratifiedKFold
import pandas
from scipy.optimize import differential_evolution
from Java.PisaFlix.src.main.resources.datamining.scripts.preprocessing import tf_idf_preprocessing

# GLOBAL PARAMETERS (Necessari per tenere traccia delle generazioni degli agenti)
dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")
pop = [0, 1, 0]
pop_size = 15


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

        log = open("../resources/elaborations/log.txt", "a+")
        log.write(
            "\n" + "\n--- End of generation: " + str(pop[1]) + "\n" + "         BEST ACC: " + str(pop[2]) + "\n\n")
        log.close()

        pop[2] = 0
        pop[1] += 1

    print("- Pop: " + str(pop[0]) + " Generation: " + str(pop[1]))

    log = open("../resources/elaborations/log.txt", "a+")
    log.write("\n" + "- Pop: " + str(pop[0]) + " Generation: " + str(pop[1]))
    log.close()

    if min_df > max_df:
        print("         Inconsistent parameters")

        log = open("../resources/elaborations/log.txt", "a+")
        log.write("\n" + "          Inconsistent parameters")
        log.close()

        return 2.0

    try:
        data = tf_idf_preprocessing(dataset=dataset, min_df=min_df, max_df=max_df,
                                    max_features=max_features)
    except:

        log = open("../resources/elaborations/log.txt", "a+")
        log.write("\n" + "          No terms for those parameters")
        log.close()

        print("         No terms for those parameters")
        return 2.0

    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    random_forest = RandomForestClassifier(criterion="entropy")

    CV_ACC = []
    mean_acc = 0
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
        print("         Error in the regression")

        log = open("../resources/elaborations/log.txt", "a+")
        log.write("\n" + "          Error in the regression")
        log.close()

        return 2.0

    print("         Arguments: min_df=" + str(min_df) + ", max_df=" + str(max_df) +
          ", max_features=" + str(max_features) + "\n           RESULT: " + str(mean_acc))

    log = open("../resources/elaborations/log.txt", "a+")
    log.write("\n" + "          Arguments: min_df=" + str(min_df) + ", max_df=" + str(max_df) +
              ", max_features=" + str(max_features) + "\n           RESULT: " + str(mean_acc))
    log.close()

    log = open("../resources/elaborations/log.csv", "a+")
    log.write("\n" + str(min_df) + "," + str(max_df) + "," + str(max_features) + "," + str(mean_acc))
    log.close()

    if pop[2] < mean_acc:
        pop[2] = mean_acc

    return 1.0 - mean_acc


if __name__ == '__main__':
    warnings.filterwarnings("ignore")
    log = open("../resources/elaborations/log.csv", "w+")
    log.write("min_df,max_df,max_features,accuracy")
    log.close()

    log = open("../resources/elaborations/log.txt", "w+")
    log.write("DIFFERENTIAL EVOLUTION TEXT LOG\n")
    log.close()

    bounds = [(0, 1), (0, 1), (38, 1400)]  # (1)
    differential_evolution(classification, bounds, popsize=pop_size, workers=-1)

# NOTES:
#   (1): Il numero di fattori consigliato va dalla radice del numero di campioni, se i fattori sono fortemente
#   correlati, fino al numero di campioni meno uno se questi sono indipendenti.
#
#   La variazione messima tra i parametri di "min_df" e "max_df" è stata settata per testare ogni possibile combinazione
#   di frequenza delle parole nell'insieme dei campioni. Ciò può portare a delle inconsistenze nei parametri che vengono
#   gestite scartando l'agente che le contiene (gli viene dato un risultato elevato).
