import os
import math
import gzip

TOTAL_NODES = 98347590
new_node = 0

comp_file = gzip.open("compressed_struct.gz","wb")

parts = os.listdir('parts')
files = []
for filename in parts:
    files.append(open(f'parts/{filename}',"r"))

lines = []
for x in files:
    lines.append(x.readline().replace('\n',''))

last_id = math.inf
act_line = []
agregar_fila = False

write_line = False
while True:
    i = 0
    done_files = True
    for i in range(len(lines)):
        if lines[i].strip():
            done_files = False
            continue
        lines[i] = files[i].readline().replace('\n','')

    if done_files:
        # TODO Escribir archivo
        text = ' '.join(list(map(lambda x: str(x),act_line))) + '\n'
        comp_file.write(text.encode())
        new_node += 1
        print("\n")
        exit()

   
    index = 0

    done_add_line = False
    write_line = True

    local_last_id = math.inf
    local_index = 0
    local_act_line = []
    while index < len(lines):
        
        # línea vacía ''
        if not lines[index].strip():
            lines[index] = files[index].readline()
        
        # línea sigue vacía ''
        if not lines[index].strip():
            index += 1
            continue
        
        # línea tiene valores
        val_line = lines[index].split()
        val_line = map(lambda x:int(x), val_line)
        val_line = list(val_line)

        if int(val_line[0]) == last_id: # compara id actual con el de la línea
            act_line[1] = act_line[1] + val_line[1]
            act_line = act_line + val_line[2:]
            lines[index] = ''
            lines[index] = files[index].readline().replace('\n','')
            write_line = False
            done_add_line = True
            break
       
        elif int(val_line[0]) < local_last_id:
            local_last_id = int(val_line[0])
            local_act_line = val_line
            local_index = index
            write_line = True

        index += 1

    
    if write_line and not done_add_line:
        # TODO Escribir archivo
        if act_line != []:
            text = ' '.join(list(map(lambda x: str(x),act_line))) + '\n'
            new_node += 1
            comp_file.write(text.encode())
        act_line = local_act_line
        last_id = local_last_id
        lines[local_index] = files[local_index].readline().replace('\n','')  
    
    
    print(f"{new_node} / {TOTAL_NODES} | {(new_node * 100) // TOTAL_NODES}%",end='\r')



    
