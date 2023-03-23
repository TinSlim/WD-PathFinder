# --------------------------
# @author Cristóbal Torres G.
# @github Tinslim
# --------------------------

import gzip
from itertools import islice
import csv

# Constantes
TOTAL_LINES_FILE = 702544222
INPUT_FILE = 'star.nt.gz'
FOLDER_PARTS = 'parts2'
MAX_LINES_PER_FILE = 100000000

def structToText(key, value):
	"""
	Convierte una estructura {1 : [2,3]} a una lista que se escribirá después
	key		1
	value	[2,3]
	ret		[1, '2.3']
	"""
	groups = list(map(lambda x: '.'.join( list(map(lambda y: str(y),x)) ),value))
	return [key] + groups

line_counter = 0		# Líneas agregadas para 1 archivo (se usa para saber cuando escribir)
line_added = 0			# Líneas agregadas totales

rel = {}				# Diccionario que almacena valores y después escribirá en archivo
act_part = 0			# Número actual del archivo en que se escribirá


with gzip.open(INPUT_FILE,'r') as f:
	# Para cada línea del archivo
	for line in f:
		# Descomprime y separa línea
		line_decode = line.decode()
		line_split = line_decode.split(" ")
		
		# Obtiene valores de la tripleta
		subj = int(line_split[0][33:-1].replace('Q',''))
		pred = int(line_split[1][38:-1].replace('P',''))
		obj = int(line_split[2][33:-1].replace('Q',''))
		
		# Si el nodo subj existe en lo que va a escribir, debe fusionar
		if subj in rel:
			done = False
			
			# Revisa si ya posee ese id de arista para fusionar
			for relation in rel[subj]:
				if relation[0] == pred:
					relation.append(obj)
					done = True
					break
			# Si no fusiona, la agrega
			if not done:
				rel[subj].append([pred,obj])
		# Nodo no existía, se crea
		else:
			rel[subj] = [[pred,obj]]


		# Este caso es similar pero para el objeto , la relación 
		# se multiplica por -1 para indicar distinto sentido
		
		# Si el nodo obj existe en lo que va a escribir, debe fusionar
		if obj in rel:
			done = False

			# Revisa si ya posee ese id de arista para fusionar
			for relation in rel[obj]:
				if relation[0] == pred * -1:
					relation.append(subj)
					done = True
					break
			# Si no fusiona, la agrega
			if not done:
				rel[obj].append([pred * -1,subj])
		# Nodo no existía, se crea
		else:
			rel[obj] = [[pred * -1,subj]]
		

		line_added += 1
		line_counter += 1

		# Si la cantidad de líneas supera MAX_LINES_PER_FILE se debe escribir un archivo
		if line_added > MAX_LINES_PER_FILE:
			# Abre archivo
			file_open = open(f"{FOLDER_PARTS}/part_{act_part}.csv",'w',newline='')
			file_writer = writer = csv.writer(file_open,delimiter=' ', quotechar='|')

			# Escribe data
			for data in sorted(rel.keys()):
				result = structToText(data,rel[data])
				file_writer.writerow(result)
			
			# Cierra archivo y reinicia variables
			file_open.close()
			rel = {}
			line_added = 0
			act_part += 1

		# Imprime Avance de las Aristas
		print(f"{line_counter} / {TOTAL_LINES_FILE} | {(line_counter * 100) // TOTAL_LINES_FILE}%",end='\r')

# Si quedaron líneas, se escribe última parte
if line_added > 0:
	# Abre archivo
	file_open = open(f"{FOLDER_PARTS}/part_{act_part}.csv",'w',newline='')
	file_writer = writer = csv.writer(file_open,delimiter=' ', quotechar='|')
	
	# Escribe data
	for data in sorted(rel.keys()):
		result = structToText(data,rel[data])
		file_writer.writerow(result)
	
	# Cierra archivo y limpia rel
	file_open.close()
	rel = {}
	act_part += 1

# Resultado final
print("\n")
print(f"Total {line_counter} lineas leídas")