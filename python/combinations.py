
import csv
import itertools
import json

max_size = 3

#------------------------------------------------
# CSV lo transforma a diccionario
values = {}
value_group = {}
with open("items.csv","r") as csv_file:
	csv_reader = csv.reader(csv_file, delimiter=',')
	for row in csv_reader:
		ids = []
		for num in row[1:]:
			ids.append(int(num))
			value_group[int(num)] = row[0]
		values[row[0]] = ids
#------------------------------------------------



#------------------------------------------------
# Genera combinaciones en un mismo grupo
same_group = {}
for size in range(max_size + 1)[2:]:
	same_group[size] = {}

	for group in values.keys():
		same_group[size][group] = []
		for subset in itertools.combinations(values[group], size):
			same_group[size][group].append(list(subset))

# Combinaciones distintos grupos
dif_group = {}
for size in range(max_size + 1)[2:]:
	dif_group[size] = []
	for groups in itertools.combinations(values.keys(), size):
		total_nums = []
		for team in groups:
			total_nums += values[team]
		for subset in itertools.combinations(total_nums,size):
			typo = []
			pass_next = False

			for x in subset:
				if value_group[x] in typo:
					pass_next = True
					break
				else:
					typo.append(value_group[x])

			if not pass_next:
				dif_group[size].append(list(subset))
#------------------------------------------------



#------------------------------------------------
# Genera Archivos
with open("../rdf-entity-path/src/main/resources/test/json_same_group.json", "w") as outfile:
	json.dump(same_group, outfile)

with open("../rdf-entity-path/src/main/resources/test/json_dif_group.json", "w") as outfile:
	json.dump(dif_group, outfile)
#------------------------------------------------