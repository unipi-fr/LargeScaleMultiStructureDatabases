import warnings
import pandas
import matplotlib.pyplot as plt
import numpy as np
from scipy.spatial.distance import cdist
from sklearn.cluster import OPTICS, AgglomerativeClustering, KMeans
from sklearn.metrics import silhouette_score


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


def show_silhouette_elbow_AgglomerativeClustering(min_cluster_size, max_cluster_size, X, linkage):
    silhouette = []

    K = []
    for i in range(min_cluster_size, max_cluster_size + 1):
        K.insert(len(K), round(len(X) / i))
        clustering_model = AgglomerativeClustering(n_clusters=round(len(X) / i),
                                                   affinity='euclidean',
                                                   linkage=linkage)  # linkage{“ward”, “complete”, “average”, “single”}
        clusters = clustering_model.fit_predict(X)
        silhouette.append(silhouette_score(X, clusters) / X.shape[0])

    # Plot the elbow
    print(K)
    plt.plot(K, silhouette, 'bx-')
    plt.xlabel('k')
    plt.ylabel('Silhouette')
    plt.title('Elbow Graph')
    plt.show()


if __name__ == '__main__':
    warnings.filterwarnings("ignore")
    data = pandas.read_csv("../resources/datasets/preprocessedData.csv")
    data.drop('MPAA', axis=1, inplace=True)
    data.drop('Year', axis=1, inplace=True)
    data.drop('Plot', axis=1, inplace=True)

    X = data.iloc[:, 1:-1].values

    # show_optics_graph(X, 6)

    show_silhouette_elbow_AgglomerativeClustering(2, 100, X, "average")
