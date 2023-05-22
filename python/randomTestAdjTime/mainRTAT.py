import gzip
import numpy as np

SUBSET = [100000, 1000000, 10000000]
VALUES = 10000

for subset in SUBSET:
    ids = []
    edges = []
    print(f"Empieza {subset}")

    with gzip.open(f"subset{subset}_compressed.gz") as file:
        for l in file:
            line = l.decode()
            line_split = line.split(" ")
            id_actual = int(line_split[0])
            total_edges = 0
            for edge in line_split[1:]:
                total_edges += len(edge.split(".")) - 1

            ids.append(id_actual)
            edges.append(total_edges)
            print(id_actual,end="\r")

        print("\nCalculando probabilidades...")
        edges_array = np.array(edges)
        prob = edges_array / (sum(edges_array))
        #prob = [w/sum(edges) for w in edges]
        print("Obteniendo opciones...")
        ans = np.random.choice(ids, size=VALUES, replace=False, p=prob)
        ans.tofile(f"subset{subset}_random_values.csv", sep = ',')
        print(f"subset{subset}_random_values.csv...\n")