# WD-PathFinder (python)

## Generación de archivos (prearchivo)
### Archivo inicial

Se requiere un archivo en formato `.nt.gz`, es decir formato NTRIPLES comprimido:

```
<https://wikidata.org/entity/Q9> <https://wikidata.org/porp/direct/P36> <https://wikidata.org/entity/Q3> .
<https://wikidata.org/entity/Q9> <https://wikidata.org/porp/direct/P36> <https://wikidata.org/entity/Q8> .
<https://wikidata.org/entity/Q3> <https://wikidata.org/porp/direct/P6> <https://wikidata.org/entity/Q9> .
```

Este se debe limpiar, eliminando aristas que no unan entidades. Para esto se usa `make_subset.py`.

### Caché Adyacente

Este caché se obtiene usando el archivo `make_pre_compressed.py`, se debe reemplazar el nombre del archivo que se lee según el que desee. Se genera un archivo que indica el ID del nodo y las aristas con las que conecta con otros nodos.

A partir del ejemplo anterior se obtendría el siguiente formato en una versión comprimida:

```
3 -36.9 6.9
8 -36.9 
9 36.3.8 -6.3
```

### Caché Triple

Este caché se obtiene usando el archivo `make_pre_native.py`, se debe reemplazar el nombre del archivo que se lee según el que desee. Se genera un archivo que une el ID del nodo con los índices de las aristas en las que participa.

A partir del ejemplo anterior se obtendría el siguiente formato en una versión comprimida:

```
3 0 2
8 1
9 0 1 2
```


## Generación valores aleatorios (randomTestAdjTime)

El archivo `mainRTAT.py` recibe un archivo en formato caché adyacente. A partir de este genera un archivo con nodos aleatorios con probabilidad según el número de aristas de cada uno.

## Generación grupos (randomTestPathTime)

El archivo `mainRTPT.py` recibe los valores aleatorios obtenidos con `mainRTAT.py` y genera grupos de nodos (2, 3, 4 y 5).


