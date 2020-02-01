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

# GLOBAL PARAMETERS (Necessario perchè non può essere passato come parametro)
dataset = pandas.read_csv("../resources/datasets/labelledData.csv", ";")


def classification(n_terms):
    # Viene utilizzato lo stesso dataset per ogni passaggio
    global dataset

    # Vengono accettati solo valori interi per le feature da selezionare
    max_features = int(round(n_terms[0]))

    # Viene eseguito il preprocessing
    data = select_k_best_preprocessing(dataset, chi2, max_features)

    # Non si considerano le colonne superflue
    data.drop('Title', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    # Si scindono i dati nella matrice delle features e in quella delle classi
    X = data.iloc[:, 1:-1].values
    y = data['MPAA']

    # Preparazione dell'algoritmo di classificazione per cui si effettua l'ottimizzazione dei parametri
    random_forest = RandomForestClassifier(criterion="entropy")

    # 10 esecuzioni di una stratified 10-fold cross validation per avere dei risultati stabili per stessi valori di k
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
        # In caso di problemi nella classificazione (Che possono accadere in regressori) si assegna un valore alto
        # alla funzione da ottimizzare
        print("Error in classification")
        return 2.0

    # I risultati vengono sta,pati a schermo e in un file csv in maniera incrementale
    print(str(max_features) + " " + str(mean_acc))
    log = open("../resources/elaborations/optimization_log.csv", "a+")
    log.write("\n" + str(max_features) + "," + str(mean_acc))
    log.close()

    # Viene restituito l'error rate medio delle stratified cross validation
    return 1.0 - mean_acc


if __name__ == '__main__':
    # Per evitare di riempire l'uscita di warnings
    warnings.filterwarnings("ignore")

    # Viene creato il file di log o ripulito
    log = open("../resources/elaborations/optimization_log.csv", "w+")
    log.write("terms,accuracy")
    log.close()

    # Vengono definiti gli intervalli entro il quale eseguire l'esplorazione dei valori dei parametri
    bounds = [(38, 1400)]  # (1)
    # Un algoritmo brute force permette di esplorare lo spazio di tutte le possibili combinazioi dei parametri, è
    # sconsigliato se non si ha un numero finito di possibili combinazioni (ma che si ha in questo caso)
    brute(classification, bounds, workers=-1)

# NOTE:
#   (1): Il numero di fattori consigliato va dalla radice del numero di campioni, se i fattori sono fortemente
#   correlati, fino al numero di campioni meno uno se questi sono indipendenti.
