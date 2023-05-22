import csv

SUBSET = ["subset100000","subset1000000", "subset10000000","subset100000000","latest-truthy_small"]
GROUPS = ["double","triple","cuadra","penta"]
GROUPS_NUM = [2,3,4,5]

# Para cada subset
for subset in SUBSET:

    print(f"Empieza {subset}")
    
    outputs = []
    writers = []
    for x in range(len(GROUPS)):
        outputs.append(open(f"randomGroups/{subset}_random_{GROUPS[x]}.csv","w",newline=""))
        writers.append(csv.writer(outputs[x],delimiter=";"))
    
    max_num = max(GROUPS_NUM)

    # Abre randomvalues del subset
    with open(f"{subset}_random_values.csv","r") as file:
        # Es solo una línea
        for line in file:
            values = list(map(lambda x:int(x),line.split(",")))
            
            # Para cada grupo
            for x in range(len(GROUPS)):
                
                i = 0
                pairs = []
                while (i < 100 * max_num):
                    text = []
                    for j in range(GROUPS_NUM[x]):
                        text.append(f"{values[i+j]}")
                    
                    pairs.append(",".join(text))
                    i+=max_num
                
                writers[x].writerow(pairs)

    for x in outputs:
        x.close()