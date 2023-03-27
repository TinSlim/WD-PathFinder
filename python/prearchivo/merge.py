# --------------------------
# @author Cristóbal Torres G.
# @github Tinslim
# --------------------------

import os
import math
import gzip

# CONSTANTES
from vars import *

def mergeDictionary(dict_1, dict_2):
    """
    Une diccionarios de formato correcto
    dict1   {0: [2,4], 1: [5]}
    dict2   {0: [3], 2: [5]}
    ret     {0: [2,4,3], 1: [5], 2: [5]}
    """
    dict_3 = {**dict_1, **dict_2}
    for key, value in dict_3.items():
        if key in dict_1 and key in dict_2:
            dict_3[key] = value + dict_1[key]
    return dict_3

def format_line (line):
    """
    Convierte de formato correcto [num, {}] a línea para el archivo .gz
    line    [0, {0: [2,3,4], 1: [5]}]
    ret     "0 0.2.3.4 1.5\n".encode()
    """
    id_line_local = line[0]
    answer = [str(id_line_local)]
    for edge in line[1].keys():
        nums_string = list(map (lambda x: str(x), line[1][edge]))
        nums_join = '.'.join([str(edge)] + nums_string)
        answer.append(nums_join)
    last = ' '.join(answer) + '\n'
    return last.encode()

def line_convert (line):
    """
    Convierte texto de línea a formato correcto [num, {}]
    line    "0 0.2.3.4 1.5"
    ret     [0, {0: [2,3,4], 1: [5]}]
    """
    line_to = []
    if line != '':
        line_to = line.split(' ')
        id_line = int(line_to[0])
        rs = {}
        for val in line_to[1:]:
            val_split = list(map(lambda x: int(x),val.split('.')))
            rs[val_split[0]] = val_split[1:]
        line_to = [id_line,rs]
    return line_to

# Vars
new_node = 0

# Archivo que se va a escribir
comp_file = gzip.open(OUTPUT,"wb")



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
        
        # Finaliza
        print("\n")
        print(f"Total Nodos: {new_node}")
        print("\n")
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
    
    # Imprime Avance de los nodos
    print(f"{new_node} / {TOTAL_NODES} | {(new_node * 100) // TOTAL_NODES}%",end='\r')



    
