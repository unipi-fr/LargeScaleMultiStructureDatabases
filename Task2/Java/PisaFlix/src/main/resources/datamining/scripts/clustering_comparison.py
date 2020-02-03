import warnings
from collections import Counter
import pandas
import matplotlib.pyplot as plt
import numpy as np
from scipy.cluster.hierarchy import dendrogram
from scipy.spatial.distance import cdist
from sklearn.cluster import OPTICS, AgglomerativeClustering, KMeans
from sklearn.metrics import silhouette_score
from scipy.cluster import hierarchy


# Funzione per la creazione del dendogramma del clustering
def plot_dendrogram(model, **kwargs):
    children = model.children_
    distance = np.arange(children.shape[0])
    no_of_observations = np.arange(2, children.shape[0] + 2)
    linkage_matrix = np.column_stack([children, distance, no_of_observations]).astype(float)
    dendrogram(linkage_matrix, **kwargs)


# Funzione per effettuare un'analisi OPTICS ed evidenziare la struttura dei cluster
def show_optics_graph(X, samples):
    clust = OPTICS(min_samples=samples)
    clust.fit(X)

    space = np.arange(len(X))
    reachability = clust.reachability_[clust.ordering_]
    labels = clust.labels_[clust.ordering_]

    plt.figure(figsize=(10, 7))
    ax1 = plt.subplot()

    ax1.plot(space[labels == -1], reachability[labels == -1], 'k.', alpha=0.3)
    ax1.set_ylabel('Reachability (epsilon distance)')
    ax1.set_ylabel('Clusters')
    ax1.set_title('Reachability Plot')

    plt.tight_layout()
    plt.show()


# Funzione per il calcolo e il plotting del grafico a gomito delle silhouette
def show_silhouette_elbow_AgglomerativeClustering(min_cluster_size, max_cluster_size, X, linkage):
    silhouette = []
    K = []
    for i in range(min_cluster_size, max_cluster_size + 1):
        K.insert(len(K), round(len(X) / i))
        clustering_model = AgglomerativeClustering(n_clusters=round(len(X) / i),
                                                   affinity='euclidean',
                                                   linkage=linkage)  # linkage{“ward”, “complete”, “average”, “single”}
        clusters = clustering_model.fit_predict(X)
        silhouette.append(silhouette_score(X, clusters))

    # Plot the elbow
    plt.plot(K, silhouette, 'bx-')
    plt.xlabel('Clusters')
    plt.ylabel('Silhouette')
    plt.title('Elbow Silhouette Graph')
    plt.gca().invert_xaxis()
    plt.show()


if __name__ == '__main__':
    warnings.filterwarnings("ignore")

    # Viene prelevato il dataset elaborato e vengono scartate le features non necessarie
    data = pandas.read_csv("../resources/datasets/preprocessedData.csv")
    data.drop('MPAA', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)
    data.drop('Title', axis=1, inplace=True)

    X = data.iloc[:, 0:].values

    # Se si vuole visualizzare il grafico OPTICS
    # show_optics_graph(X, 6)

    # Se si vogliono visualizzare i singoli grafici sulla silhouette
    # show_silhouette_elbow_AgglomerativeClustering(2, 20, X, "single")
    # show_silhouette_elbow_AgglomerativeClustering(2, 20, X, "average")
    # show_silhouette_elbow_AgglomerativeClustering(2, 20, X, "complete")
    # show_silhouette_elbow_AgglomerativeClustering(2, 20, X, "ward")

    # Se si vuole visualizzare il dendogramma di un clustering particolare
    # clustering_model = AgglomerativeClustering(affinity='euclidean',
    #                                             linkage="complete")  # linkage{“ward”, “complete”, “average”, “single”}
    #
    # model = clustering_model.fit(X)
    # plt.title('Hierarchical Clustering Dendrogram')
    # plot_dendrogram(clustering_model, labels=clustering_model.labels_, p=2, truncate_mode='level')
    # plt.show()

    # Preparazione variabili per il confronto
    s_agg_ward = []
    s_agg_com = []
    s_agg_avg = []
    s_agg_sl = []
    s_kmeans = []
    distortions = []
    K = []

    # File dove viene salvato il numero massimo e minimo di componenti per ogni cluster
    log = open("../resources/elaborations/clusterVariance.csv", "w+")
    log.write("Method,Min,Max,Var,k")
    log.close()

    # Calcolo della silhouette e del numero massimo e minimo delle componenti di ogni cluster per ogni algoritmo
    # Si utilizza un valore di n/k da 2 a 20 (film per cluster circa)
    for i in range(2, 21):
        print("i = " + str(i))
        # Si salva il valore di k utilizzato a questo passo
        K.insert(len(K), round(len(X) / i))

        # Agglomerativo con ward-linkage
        clustering_model = AgglomerativeClustering(n_clusters=round(len(X) / i),
                                                   affinity='euclidean',
                                                   linkage='ward')
        clusters = clustering_model.fit_predict(X)

        s_agg_ward.append(silhouette_score(X, clusters))

        # Calcolo della varianza dei cluster
        tmp = data.copy()
        tmp.insert(0, "Cluster", clustering_model.labels_)
        centers = pandas.DataFrame()
        for c in range(0, clustering_model.n_clusters):
            cluster = tmp[tmp["Cluster"] == c]
            cluster.drop("Cluster", axis=1, inplace=True)
            centers = centers.append(cluster.mean(), ignore_index=True)
        variance = sum(np.min(cdist(X, centers, 'euclidean'), axis=1))

        count = Counter(clustering_model.labels_).values()
        n_max = max(count)
        n_min = min(count)
        log = open("../resources/elaborations/clusterVariance.csv", "a+")
        log.write('\n' + "agg_ward," + str(n_min) + "," + str(n_max) + "," + str(variance) + "," + str(round(len(X) / i)))
        log.close()

        # Agglomerativo con complete-linkage
        clustering_model = AgglomerativeClustering(n_clusters=round(len(X) / i),
                                                   affinity='euclidean',
                                                   linkage='complete')

        clusters = clustering_model.fit_predict(X)
        s_agg_com.append(silhouette_score(X, clusters))

        # Calcolo della varianza dei cluster
        tmp = data.copy()
        tmp.insert(0, "Cluster", clustering_model.labels_)
        centers = pandas.DataFrame()
        for c in range(0, clustering_model.n_clusters):
            cluster = tmp[tmp["Cluster"] == c]
            cluster.drop("Cluster", axis=1, inplace=True)
            centers = centers.append(cluster.mean(), ignore_index=True)
        variance = sum(np.min(cdist(X, centers, 'euclidean'), axis=1))

        count = Counter(clustering_model.labels_).values()
        n_max = max(count)
        n_min = min(count)
        log = open("../resources/elaborations/clusterVariance.csv", "a+")
        log.write(
            '\n' + "agg_compl," + str(n_min) + "," + str(n_max) + "," + str(variance) + "," + str(round(len(X) / i)))
        log.close()

        # Agglomerativo con average-linkage
        clustering_model = AgglomerativeClustering(n_clusters=round(len(X) / i),
                                                   affinity='euclidean',
                                                   linkage='average')

        clusters = clustering_model.fit_predict(X)
        s_agg_avg.append(silhouette_score(X, clusters))

        # Calcolo della varianza dei cluster
        tmp = data.copy()
        tmp.insert(0, "Cluster", clustering_model.labels_)
        centers = pandas.DataFrame()
        for c in range(0, clustering_model.n_clusters):
            cluster = tmp[tmp["Cluster"] == c]
            cluster.drop("Cluster", axis=1, inplace=True)
            centers = centers.append(cluster.mean(), ignore_index=True)
        variance = sum(np.min(cdist(X, centers, 'euclidean'), axis=1))

        count = Counter(clustering_model.labels_).values()
        n_max = max(count)
        n_min = min(count)
        log = open("../resources/elaborations/clusterVariance.csv", "a+")
        log.write(
            '\n' + "agg_avg," + str(n_min) + "," + str(n_max) + "," + str(variance) + "," + str(round(len(X) / i)))
        log.close()

        # Agglomerativo con single-linkage
        clustering_model = AgglomerativeClustering(n_clusters=round(len(X) / i),
                                                   affinity='euclidean',
                                                   linkage='single')

        clusters = clustering_model.fit_predict(X)
        s_agg_sl.append(silhouette_score(X, clusters))

        # Calcolo della varianza dei cluster
        tmp = data.copy()
        tmp.insert(0, "Cluster", clustering_model.labels_)
        centers = pandas.DataFrame()
        for c in range(0, clustering_model.n_clusters):
            cluster = tmp[tmp["Cluster"] == c]
            cluster.drop("Cluster", axis=1, inplace=True)
            centers = centers.append(cluster.mean(), ignore_index=True)
        variance = sum(np.min(cdist(X, centers, 'euclidean'), axis=1))

        count = Counter(clustering_model.labels_).values()
        n_max = max(count)
        n_min = min(count)
        log = open("../resources/elaborations/clusterVariance.csv", "a+")
        log.write(
            '\n' + "agg_singl," + str(n_min) + "," + str(n_max) + "," + str(variance) + "," + str(round(len(X) / i)))
        log.close()

        # KMeans
        clustering_model = KMeans(n_clusters=round(len(X) / i), algorithm="full")

        clusters = clustering_model.fit_predict(X)
        s_kmeans.append(silhouette_score(X, clusters))

        # Calcolo della varianza dei cluster
        variance = sum(np.min(cdist(X, clustering_model.cluster_centers_, 'euclidean'), axis=1))

        count = Counter(clustering_model.labels_).values()
        n_max = max(count)
        n_min = min(count)
        log = open("../resources/elaborations/clusterVariance.csv", "a+")
        log.write(
            '\n' + "k-means," + str(n_min) + "," + str(n_max) + "," + str(variance) + "," + str(round(len(X) / i)))
        log.close()

    # Grafico "a gomito" della silhouette
    plt.plot(K, s_agg_ward, 'bx-', color="green", label='Agg. Ward')
    plt.plot(K, s_agg_com, 'bx-', color="red", label='Agg. Complete')
    plt.plot(K, s_agg_avg, 'bx-', color="blue", label='Agg. Average')
    plt.plot(K, s_agg_sl, 'bx-', color="violet", label='Agg. Single')
    plt.plot(K, s_kmeans, 'bx-', color="black", label='K-means')
    plt.xlabel('Clusters')
    plt.ylabel('Silhouette')
    plt.title('Elbow Silhouette Graph')
    plt.legend()
    plt.gca().invert_xaxis()
    plt.show()

    # Vengono stampati anche gli effettivi valori
    print("Ward: " + str(s_agg_ward))
    print("Complete: " + str(s_agg_com))
    print("Average: " + str(s_agg_avg))
    print("Single: " + str(s_agg_sl))
    print("K-means: " + str(s_kmeans))
