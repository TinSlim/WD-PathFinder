# RDF-Path-server

## Información

- Lanzar en el servidor demora **80 minutos**.
- Parámetros extra, se agregan con **-D{lo de abajo}**:
	
	- Archivo grafo externo a `.jar`: `graph-path="{nombre archivo}"`
	- Token de Telegram para recibir mensajes de carga de grafo: `tg-token="{token}"`


## Lanzar Servidor desde `.jar`

Primero se debe compilar front, luego lanzar el sevidor:

### Compilar Front

- En archivo `.env` asignar las direcciones `http` y `ws`. En caso de usar cerificado `SSL`, direcciones deben ser `https` y `wss`.

- Compilar archivos, estos se guardarán en la carpeta resources del proyecto en Java.
```
cd .\frontend\
npm run build
```

### Construir archivo Jar

- Ejecutar lo siguiente para armar el archivo `.jar`. Este se almacenará en `\rdf-entity-path\target`.
```
cd ..\rdf-entity-path\
.\mvnw package
```

### Lanzar

Se tienen dos opciones.

- El compilado incluye un archivo de grafos, si se quiere usar ese, ejecutar:
```
java -jar .\rdf-entity-path-0.0.1-SNAPSHOT.jar
```

- Usar un archivo `.gz` externo al archivo jar. Se debe tener este al lado del archivo.

```
java -jar -Dgraph-path="{nombre archivo}" .\rdf-entity-path-0.0.1-SNAPSHOT.jar

#EJEMPLO:
java -jar -Dgraph-path="delete.nt.gz" .\rdf-entity-path-0.0.1-SNAPSHOT.jar
```


## Ejecución Tests

### Armar conjuntos

En la carpeta `python` crear archivo `items.csv` que tenga id de entidades y su agrupación.

Ejecutar `python combinations.py`.

Con esto almacenará combinaciones en `rdf-entity-path/src/main/resuorces/test`.

### Ejecutar tests de tiempo

Con los archivos creados ejecutar `TimeTest.java`. Creara resultados en la carpeta `rdf-entity-path/src/main/resuorces/test/results`.