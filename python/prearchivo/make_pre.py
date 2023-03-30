# --------------------------
# @author Cristóbal Torres G.
# @github Tinslim
# --------------------------

import os
import math
import gzip
from itertools import islice
import time
import csv

from utils import *
from vars import *

inicio = time.time()

#----------------------------------------------------------------------------------------------
line_counter = 0		# Líneas agregadas totales
line_added = 0			# Líneas agregadas para 1 archivo (se usa para saber cuando escribir)
line_file = 0           # Líneas totales del archivo (reales)

rel = {}				# Diccionario que almacena valores y después escribirá en archivo
act_part = 0			# Número actual del archivo en que se escribirá

with gzip.open('latest-truthy.nt.gz','r') as f:
    # Para cada línea del archivo
	for line in f:
		line_file += 1
		# Descomprime y separa línea
		line_decode = line.decode()
		line_split = line_decode.split(" ")
		
        # Revisa formato correcto de la línea
		if ('<http://www.wikidata.org/entity/Q' != line_split[0][:33] or
			'<http://www.wikidata.org/prop/direct/P' != line_split[1][:38] or
			'<http://www.wikidata.org/entity/Q' != line_split[2][:33]):
			continue
		
        # Obtiene valores de la tripleta
		subj = int(line_split[0][33:-1])
		pred = int(line_split[1][38:-1])
		obj = int(line_split[2][33:-1])
		
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
		line_added +=1
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

termino_dict_native = time.time()
print(f"Lineas usadas del archivo: {line_counter}")
print(f"Líneas reales archivo: {line_file}")

#=================================================================================================
#=================================================================================================
#=================================================================================================
#=================================================================================================
#=================================================================================================
#=================================================================================================

inicio_merge = time.time()

# Vars
new_node = 0

# Archivo que se va a escribir
comp_file = gzip.open('latest-truthy_compressed.gz',"wb")
# Abro archivos
parts = os.listdir(FOLDER_PARTS)
files = []
for filename in parts:
    files.append(open(f'{FOLDER_PARTS}/{filename}',"r"))

# Lee una línea de cada archivo
lines = []              # Almacena lineas 1 línea cargada de cada archivo
for x in files:
    line_to = x.readline().replace('\n','')
    line_f = line_convert(line_to)
    lines.append(line_f)



act_line = []           # Línea que se escribirá en archivo
last_id = math.inf      # Menor ID de líneas escritas en archivo
write_line = False      # Debe escribir línea

# En algún momento se ejecuta exit()
while True:

    # Revisa líneas cargadas, si estan todas vacías [], hay que terminar
    i = 0
    done_files = True           # Condición de término
    for i in range(len(lines)):
        if lines[i] != []:
            done_files = False  # Se define que va a terminar
            continue
        line_to = files[i].readline().replace('\n','')
        line_f = line_convert(line_to)
        lines[i] = line_f


    # Todas vacías, debe terminar
    if done_files:
        # Escribir archivo
        line_write = format_line(act_line)
        comp_file.write(line_write)
        new_node += 1
        
        final = time.time()
        # Finaliza
        print("\n")
        print(f"Total Nodos: {new_node}")
        print(f"ID Máximo: {act_line[0]}")
        print("\n")
	
        print("Tiempo inicio: ",inicio)
        print("Tiempo final creación dict: ", termino_dict_native)
        print("Tiempo inicio merge: ", inicio_merge)
        print("Tiempo FIN: ", final)
        exit()

   
    index = 0           # Índice para obtener línea cargada del archivo
    write_line = True   # Debe escribir la línea actual

    # Valores para candidato de línea que se escribirá
    local_last_id = math.inf
    local_index = 0
    local_act_line = []


    # Revisa líneas cargadas para actualizar la que se deberá escribir (Similar a un merge sort)
    while index < len(lines):
        
        # Si la línea es vacía carga una línea de su archivo
        if lines[index] == []:
            line_to = files[index].readline().replace('\n','')
            lines[index] = line_convert(line_to)
        
        # Si sigue vacía, pasa a la siguiente línea cargada (ese archivo esta terminado)
        if lines[index] == []:
            index += 1
            continue
        
        # La línea tiene valores, debe actualizar la línea que se escribirá
        line_id = lines[index][0]
        line_con = lines[index][1]

        # Si tiene mismo id que la que se escribirá, se mezclan
        if line_id == last_id:
            act_line[1] = mergeDictionary(line_con, act_line[1])
            line_to = files[index].readline().replace('\n','')
            lines[index] = line_convert(line_to)
            write_line = False
            break
        
        # Busca una línea candidata para escribir
        elif line_id < local_last_id:
            local_last_id = line_id
            local_act_line = lines[index]
            local_index = index

        index += 1

    # Escribe línea y candidato se extrae, obteniendo una nueva línea del archivo
    if write_line:
        if act_line != []:
            # Escribir archivo
            line_write = format_line(act_line)
            comp_file.write(line_write)
            new_node += 1
        act_line = local_act_line
        last_id = local_last_id
        line_to = files[local_index].readline().replace('\n','')
        lines[local_index] = line_convert(line_to)


