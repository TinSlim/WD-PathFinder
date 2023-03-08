import json
import itertools

PREFIX = "wd:Q"
LIMIT = 2 # usar 2


#### -----
#### Hacer Querys
#### -----
def queryType1 (values, size):
	QUERY = "SELECT * WHERE {"
	vals_to_query = list(itertools.combinations(values, 2))
	MINI_QUERYS = []

	# hacer combinaciones y acumular en QUERY
	for group in vals_to_query:

		# En un grupo generará combinaciones
		x = 0
		query_group = get_query_of_two(group,size)
		MINI_QUERYS.append(query_group[0])
	
	QUERY_INSIDE = 'UNION\n'.join(MINI_QUERYS)
	big_query = 'SELECT *\nWHERE{\n' + QUERY_INSIDE + '}' # quité el DISTINCT
	return big_query

def get_query_of_two(entidades,largo_camino):
	nodo_num = 0
	arista_num = 0
	querys = []
	lista_aristas = []
	x = 0
	while x< (2**largo_camino):

		text = bin(x)[2:].zfill(largo_camino)
		#print(text) # Bits combinaciones

		aristas = [PREFIX + str(entidades[0])]
		for arista in range(largo_camino):
			aristas.append(f"?e{arista_num%largo_camino}") # ESTE CAMBIO REPITE ARISTAS (%)
			arista_num+=1
			if (len(aristas) == largo_camino * 2):
				break
			aristas.append(f"?n{nodo_num % (largo_camino - 1)}") # ESTE CAMBIO REPITE NODOS (%)
			nodo_num+=1

		aristas.append(PREFIX + str(entidades[1]))
		act_query = ""
		for letter,index in zip(text,range(largo_camino)):
			actuals = aristas[index*2:(index*2)+3]
			if letter == '0':
				act_query += f"{actuals[0]} {actuals[1]} {actuals[2]}. \n"
			else:
				act_query += f"{actuals[2]} {actuals[1]} {actuals[0]}. \n"
		
		act_query = "{\n"+act_query+"}\n"
		querys.append(act_query)
		#print(act_query)
		x+=1
		lista_aristas.append(aristas)

	big_query = 'UNION\n'.join(querys)
	# big_query = 'SELECT DISTINCT *\nWHERE{\n' + big_query + '}' SE SACA YA QUE NO QUEREMOS CERRAR AÚN LA QUERY
	# print(big_query)
	return big_query, lista_aristas	


def get_query_of_two_cnc(entidades, largo_camino):
	node = 0
	total_nodes = ["?n"+str(x) for x in range(largo_camino-1)]
	total_nodes = [PREFIX + str(entidades[0])] + total_nodes + [PREFIX + str(entidades[1])]
	mini_querys = []
	act_edge = 0
	while (act_edge < len(total_nodes) - 1):
		query = "{" + \
		f"{total_nodes[act_edge]} ?e{str(act_edge)} {total_nodes[act_edge + 1]}" + \
		"} UNION {" + \
		f"{total_nodes[act_edge + 1]} ?e{str(act_edge)} {total_nodes[act_edge]}" + \
		"}"

		mini_querys.append(query)
		act_edge += 1
	
	return "\n".join(mini_querys)


def queryType2 (values, size):
	"""
SELECT *
WHERE {
	{ wd:Q298 ?p ?z1 } UNION { ?z1 ?p wd:Q298 }
	{ ?z1 ?q wd:Q414 } UNION { wd:Q414 ?q ?z1 }
}"""
	QUERY = "SELECT * WHERE {"
	vals_to_query = list(itertools.combinations(values, 2))
	MINI_QUERYS = []

	# hacer combinaciones y acumular en QUERY
	for group in vals_to_query:

		# En un grupo generará combinaciones
		x = 0
		print(group)
		MINI_QUERYS.append(get_query_of_two_cnc(group, size))
	QUERY_INSIDE = 'UNION\n'.join(MINI_QUERYS)
	big_query = 'SELECT *\nWHERE{\n' + QUERY_INSIDE + '}' # quité el DISTINCT
	return big_query

## Ejemplo de uso:
## print(queryType2([41265, 6094390],3))

#### -----
#### Main
#### -----

if __name__ == "__main__":
	#### -----
	#### Carga Archivos
	#### -----

	dif_group_file = open("json_dif_group.json")
	dif_group_dict = json.load(dif_group_file)
	dif_group_file.close()

	same_group_file = open("json_same_group.json")
	same_group_dict = json.load(same_group_file)
	same_group_file.close()

	for size in dif_group_dict.keys():
		for group in dif_group_dict[size]:
			query = queryType2(group,3)
			###
			### Ejecutar y guardar resultado
			###

		# Detiene si llega al limite
		if LIMIT == int(size):
			break

