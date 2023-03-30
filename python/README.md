# RDF-Path-server (python)

## randomTestAdjTime

## prearchivo

### ¿Qué es?

Convierte archivos `.nt.gz`, es decir formato NTRIPLES comprimido:

```
<https://wikidata.org/entity/Q9> <https://wikidata.org/porp/direct/P36> <https://wikidata.org/entity/Q3> .
<https://wikidata.org/entity/Q9> <https://wikidata.org/porp/direct/P36> <https://wikidata.org/entity/Q8> .
<https://wikidata.org/entity/Q3> <https://wikidata.org/porp/direct/P6> <https://wikidata.org/entity/Q9> .
```

A un formato que que comprime esta información:

```
3 -36.9 6.9
8 -36.9 
9 36.3.8 -6.3
```

### Uso

1. Cambiar variables de `vars.py`.
    - `INPUT`: Es el archivo de entrada, que se va a comprimir.
    - `MAX_LINES_PER_FILE`: Líneas que va a leer cada Batch almacenado en Disco.
    - `FOLDER_PARTS`: Carpeta donde se va a almacenar cada Batch, debe existir.
    - `OUTPUT`: Nombre del archivo de salida.
    - `TOTAL_LINES_FILE` = Líneas totales del archivo `INPUT`, se usa para saber como avanza el programa.
    - `TOTAL_NODES` = Nodos totales del archivo `INPUT`, se usa para saber como avanza el programa.


## combinationsTest