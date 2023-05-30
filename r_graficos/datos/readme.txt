================ Scripts ================

archivo : merge_data.py
uso     : Copiarlo a una carpeta con .CSV y ejecutar, preguntará si desea unir y exportará como out.csv.

=======  Resultados que se usan   =======

resultados  : timeNano
unidos      : 1. timeNano.csv 
            : 2. sizeNano.csv
info        : 1. Tiempo de búsqueda vecinos para nodos usando cada estructura.
              2. Tamaño de estos grafos y tiempo que demoran en cargarse.

resultados  : OptMem
unidos      : TPF_OptMem.csv
info        : Resultados búsqueda de caminos con algoritmo optimizado.

resultados  : OptMem/lim[-1|1000|10000|100000|1000000]
unidos      : TPF_OptMem_lim[-1|1000|10000|100000|1000000].csv
info        : Resultados búsqueda de caminos con algoritmo optimizado. El valor se usa para limitar los vecinos que se agregan.
              (Ej: lim1000: nodos con 1000 vecinos, no agrega los 1000 vecinos). El -1 es sin límite.

======= Resultados que NO se usan =======

resultados  : NoOptMem
unidos      : TPF_noOptMem.csv
info        : Resultados búsqueda de caminos con algoritmo sin optimizar.

resultados  : timePathFinding
unidos      : timePathFinding.csv
info        : Resultados búsqueda de camino, anterior a plantear la optimización.

unidos      : comparision.csv
info        : Permite comparar resultados NoOptMem con NoOptMem

====

resultados  : NoOptMem
unidos      : TPF_noOptMem.csv