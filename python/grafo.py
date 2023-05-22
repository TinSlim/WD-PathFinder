# Diccionario con valores listas de enteros
diccionario = {
    "llave1": {1, 2, 3, 4},
    "llave2": {2, 3, 4},
    "llave3": {1, 2, 3},
    "llave4": {4, 5, 6},
    "llave5": {1, 2, 3},
    "llave6": {6,5,4},
}

# Diccionario para almacenar los grupos de enteros por llaves
grupos = {}

# Recorrer el diccionario
for llave, lista_enteros in diccionario.items():
    # Convertir la lista de enteros en una tupla para poder usarla como clave en el diccionario 'grupos'
    tupla_enteros = tuple(lista_enteros)

    # Verificar si la tupla de enteros ya existe en el diccionario 'grupos'
    if tupla_enteros in grupos:
        grupos[tupla_enteros].append(llave)
    else:
        grupos[tupla_enteros] = [llave]

#print(grupos)

# Imprimir los grupos de enteros por llaves
for tupla_enteros, llaves in grupos.items():
    enteros_str = ', '.join(map(str, tupla_enteros))
    llaves_str = ', '.join(llaves)
    #print(f"({enteros_str}) ({llaves_str})")


#len(llaves_str) = len(enteros_str[0])
#cantidad de llaves = cantidad de aristas por nodo

#exit(1)



# Lista de aristas
aristas = [
]

relaciones = {}

nodos = {
}

agregar = [
    [0, "33", 1],

    [1, "34", 4],
    [1, "34", 5],
    [1, "34", 6],
    [1, "34", 7],
    
    #[1, "34", 99],
    #[99, "56", 98],

    [4, "56", 2],
    [5, "56", 2],
    [6, "56", 2],
    [7, "56", 2],

    [2, "66", 3],
]

def agregar_arista (arista):
    global aristas
    global nodos
    global relaciones

    origen, etiqueta, destino = arista
    aristas.append(arista)

    clave1 = "-" + etiqueta + "_" + str(origen)
    clave2 = etiqueta + "_" + str(destino) 

    if clave1 in relaciones:
        relaciones[clave1] = relaciones[clave1].union({destino})
    else:
        relaciones[clave1] = {destino}
    if destino in nodos:
        nodos[destino] = nodos[destino] + 1
    else:  
        nodos[destino] = 1

    if clave2 in relaciones:
        relaciones[clave2] = relaciones[clave2].union({origen})
    else:
        relaciones[clave2] = {origen}
    if origen in nodos:
        nodos[origen] = nodos[origen] + 1
    else:  
        nodos[origen] = 1

for arista in agregar:
    # Diccionario para almacenar las relaciones entre etiquetas y nodos
    agregar_arista(arista)
    #print("Nodos:",nodos,"")
    #print("Aristas:",aristas,"")
    #print("Relaciones:",relaciones,"")
    #input("...")


print("Nodos:",nodos,"")
print("Aristas:",aristas,"")
print("Relaciones:",relaciones,"")

# Diccionario para almacenar los grupos de enteros por llaves


def getClusters ():
    global relaciones
   
    grupos = {}
    # Recorrer el diccionario
    for llave, lista_enteros in relaciones.items():
        # Convertir la lista de enteros en una tupla para poder usarla como clave en el diccionario 'grupos'
        tupla_enteros = tuple(lista_enteros)

        # Verificar si la tupla de enteros ya existe en el diccionario 'grupos'
        if tupla_enteros in grupos:
            grupos[tupla_enteros].append(llave)
        else:
            grupos[tupla_enteros] = [llave]

    print(grupos)

    for tupla_enteros, llaves in grupos.items():
        if len(tupla_enteros) < 3:
            continue
        
        cluster_nodes = []

        for val in tupla_enteros:
            if nodos[val] == len(llaves):
                cluster_nodes.append(val)

        print(cluster_nodes)
        #enteros_str = ', '.join(map(str, tupla_enteros))
        #llaves_str = ', '.join(llaves)
        #print(f"({enteros_str}) ({llaves_str})")


getClusters()
input("__")



exit(1)
# Imprimir los grupos de enteros por llaves
for tupla_enteros, llaves in grupos.items():
    enteros_str = ', '.join(map(str, tupla_enteros))
    llaves_str = ', '.join(llaves)
    print(f"({enteros_str}) ({llaves_str})")







# Encontrar los nodos que comparten las mismas conexiones
nodos_mismas_conexiones = []
for nodos_relacionados in relaciones.values():
    if len(nodos_relacionados) > 2:
        nodos_mismas_conexiones.extend(nodos_relacionados)

# Eliminar duplicados de la lista de nodos
nodos_mismas_conexiones = list(set(nodos_mismas_conexiones))

# Imprimir los nodos que comparten las mismas conexiones
print("Nodos que comparten las mismas conexiones:")
print(nodos_mismas_conexiones)
