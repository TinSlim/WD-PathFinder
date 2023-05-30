# RDF-Path-server

## Requisitos

- Node: v16.17.1
- Java: openjdk 11

## Preparación

- En `/frontend` usar `npm i`, para instalar dependencias.
- Cargar `/rdf-entity-path` a **Eclipse IDE**. 

## Trabajo de Datos

- Tener un archivo en formato `.nt`.

## Lanzamiento

### Lanzamiento, Producción

- Escribir en `/frontend/.env` variables de entorno.

```
BASE_URL = "https://urldeejemplo.ejemplo"		# Url de la app
WEB_SOCKET_URL = "wss://urldeejemplo.ejemplo"	# Url del WebSocket
```

- Ejecutar `build.ps1`.
- El archivo que ejecuta la aplicación se exportará en `/export`.
- Ejecutar el archivo con los parámetros:
	- `-Xmx59g` : Memoria disponible.
	- `-Dgraph-data=` : Archivo con datos para poblar el grafo.

Ejemplo a continuación:

```
java -jar -Xmx59g -Dgraph-data="latest-truthy_small" rdf-entity-path-0.0.1-SNAPSHOT.jar
```

> Considere que el formato debe ser Adyacente, de modo que si sus datos no han sido transformados debe llevarlo a cabo antes de usarlos.

### Lanzamiento, Desarrollo - Frontend

- Escribir en `/frontend/.env` variables de entorno.

```
BASE_URL = "http://localhost:8080"
WEB_SOCKET_URL = "ws://localhost:8080"
```

- Ejecutar los siguientes comandos:

```
cd frontend
npm run dev
```

- El archivo `frontend/public/index.html` mostrará los cambios en vivo.

### Lanzamiento, Desarrollo - Backend

- Escribir en `/frontend/.env` variables de entorno.

```
BASE_URL = "http://localhost:8080"
WEB_SOCKET_URL = "ws://localhost:8080"
```

- Ejecutar los siguientes comandos:

```
cd frontend
npm run build
```

- Usar `RunServer.launch` en Eclipse IDE.



## Ejecución Tests

### Armar conjuntos

En la carpeta `python` crear archivo `items.csv` que tenga id de entidades y su agrupación.

Ejecutar `python combinations.py`.

Con esto almacenará combinaciones en `rdf-entity-path/src/main/resuorces/test`.

### Ejecutar tests de tiempo

Con los archivos creados ejecutar `TimeTest.java`. Creara resultados en la carpeta `rdf-entity-path/src/main/resuorces/results`.