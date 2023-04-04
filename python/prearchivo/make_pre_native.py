# --------------------------
# @author Cristóbal Torres G.
# @github Tinslim
# --------------------------

import os
import math
import gzip
import time
import csv

MAX_LINES_PER_FILE = 100000000
INPUT_FILE  = 'subsets/subset1000000.nt.gz'
OUTPUT_FILE = 'subsets/subset1000000_native.gz'

inicio = time.time()

#-----------------------------

line_file = 0       # lineas_archivo
line_added = 0      # lineas agregadas
line_counter = 0    # lineas usadas

act_part = 0
rel = {}
index_edge = 0

with gzip.open(INPUT_FILE,'r') as f:
    for line in f:
        line_file =+ 1
        
        # Descomprime y separa linea
        line_decode = line.decode()
        line_split = line_decode.split(" ")

        # Revisa formato
        if ('<http://www.wikidata.org/entity/Q' != line_split[0][:33] or
            '<http://www.wikidata.org/prop/direct/P' != line_split[1][:38] or
            '<http://www.wikidata.org/entity/Q' != line_split[2][:33]):
            continue

        # Obtiene valores de la tripleta
        subj = int(line_split[0][33:-1])
        pred = int(line_split[1][38:-1])
        obj = int(line_split[2][33:-1])

        if subj in rel:
            rel[subj].append(index_edge)
        else:
            rel[subj] = [index_edge]

        if obj in rel:
            rel[obj].append(index_edge)
        else:
            rel[obj] = [index_edge]

        line_added += 1
        index_edge += 1
        line_counter += 1

        if line_added > MAX_LINES_PER_FILE:
            # Abre archivo
            file_open = open(f"parts/part_{act_part}.csv",'w',newline='')
            file_writer = csv.writer(file_open,delimiter=' ', quotechar='|')

            # Escribe data
            for data in sorted(rel.keys()):
                result = [data] + rel[data]
                file_writer.writerow(result)
            
            # Cierra archivo
            file_open.close()
            rel = {}
            line_added = 0
            act_part += 1

if line_added > 0:
    # Abre archivo
    file_open = open(f"parts/part_{act_part}.csv",'w',newline='')
    file_writer = csv.writer(file_open,delimiter=' ', quotechar='|')

    # Escribe data
    for data in sorted(rel.keys()):
        result = [data] + rel[data]
        file_writer.writerow(result)
    
    # Cierra archivo
    file_open.close()
    rel = {}
    line_added = 0
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
new_node = 0

# Archivo que se va a escribir
comp_file = gzip.open(OUTPUT_FILE,"wb")

# Abro archivos
parts = os.listdir('parts')
files = []
for filename in parts:
    files.append(open(f'parts/{filename}',"r"))

# Lee una línea de cada archivo
lines = []              # Almacena lineas 1 línea cargada de cada archivo
for x in files:
    line_to = x.readline().replace('\n','')
    line_to_split = line_to.split(' ')
    line_to_split[0] = int(line_to_split[0])
    lines.append(line_to_split)


act_line = []           # Línea que se escribirá en archivo
last_id = math.inf      # Menor ID de líneas escritas en archivo
write_line = False      # Debe escribir línea

# En algún momento se ejecuta exit()
while True:
    # Revisa líneas cargadas, si estan todas vacías [], hay que terminar
    i = 0
    done_files = True           # Condición de término
    for i in range(len(lines)):
        if lines[i] != [] and lines[i] != [''] :
            done_files = False  # Se define que va a terminar
            continue
        
        line_to = files[i].readline().replace('\n','')
        line_to_split = line_to.split(' ')
        if line_to_split != ['']:
            line_to_split[0] = int(line_to_split[0])
            lines[i] = line_to_split
    
    # Todas vacías, debe terminar
    if done_files:
        # Escribir archivo
        act_line[0] = str(act_line[0])
        comp_file.write((' '.join(act_line) + '\n').encode())
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
            line_to_split = line_to.split(' ')
            if line_to_split != ['']:
                line_to_split[0] = int(line_to_split[0])
            lines[index] = line_to_split
        
        # Si sigue vacía, pasa a la siguiente línea cargada (ese archivo esta terminado)
        if lines[index] == ['']:
            index += 1
            continue
        
        # La línea tiene valores, debe actualizar la línea que se escribirá
        line_id = lines[index][0]
        line_con = lines[index][1:]

        # Si tiene mismo id que la que se escribirá, se mezclan
        if line_id == last_id:
            act_line = act_line + line_con

            line_to = files[index].readline().replace('\n','')
            line_to_split = line_to.split(' ')
            if line_to_split != ['']:
                line_to_split[0] = int(line_to_split[0])
            lines[index] = line_to_split
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
            act_line[0] = str(act_line[0])
            comp_file.write((' '.join(act_line) + '\n').encode())
            new_node += 1
        act_line = local_act_line
        last_id = local_last_id
        
        line_to = files[local_index].readline().replace('\n','')
        line_to_split = line_to.split(' ')
        if line_to_split != ['']:
            line_to_split[0] = int(line_to_split[0])
        lines[local_index] = line_to_split


