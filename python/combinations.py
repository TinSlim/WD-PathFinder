
import csv
import itertools
import json

max_size = 3


#------------------------------------------------
# CSV lo transforma a diccionario
values = {}
value_group = {}
with open("../export/test_data/items.csv","r") as csv_file:
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
	ans = {}

	for group in values.keys():
		result = []
		for subset in itertools.combinations(values[group], size):
			result.append(list(subset))
		if result:
			ans[group] = result
	same_group[size] = ans

# Combinaciones distintos grupos
dif_group = {}
for size in range(max_size + 1)[2:]:
	result = []
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
				result.append(list(subset))
	if result:
		dif_group[size] = result
#------------------------------------------------



#------------------------------------------------
# Genera Archivos
with open("../export/test_data/json_same_group.json", "w") as outfile:
	json.dump(same_group, outfile)

with open("../export/test_data/json_dif_group.json", "w") as outfile:
	json.dump(dif_group, outfile)
#------------------------------------------------