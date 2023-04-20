import csv

SUBSET = ["subset100000","subset1000000", "subset10000000","subset100000000","latest-truthy_small"]

for subset in SUBSET:
    ids = []
    edges = []
    print(f"Empieza {subset}")

    output = open(f"{subset}_random_pairs.csv","w",newline="")
    writer = csv.writer(output,delimiter=";")
    with open(f"{subset}_random_values.csv","r") as file:
        for line in file:
            values = list(map(lambda x:int(x),line.split(",")))
            i = 0
            pairs = []
            while (i < 200):
                pairs.append(f"{values[i]},{values[i+1]}")
                i+=2
        writer.writerow(pairs)