# WD-PathFinder

Este proyecto desarrolla WoolNet, el sistema visual para explorar subgrafos temáticos en Wikidata.

## Requisitos

- Node: v16.17.1
- Java: openjdk 11

## Preparación

- En `/frontend` usar `npm i`, para instalar dependencias.
- Cargar `/rdf-entity-path` a **Eclipse IDE**.
- Crear datos para poblar grafo, se describe en carpeta `python`.
- Crear datos para pruebas, se describe en carpeta `python`.


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

> Considere que el formato de datos debe ser Adyacente, de modo que si sus datos no han sido transformados debe llevarlo a cabo antes de usarlos.

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

### Tests

Los archivos ubicados en `rdf-entity-path/src/main/experiments` se deben convertir en `.jar`. Se usan para ejecutar los tests.

- `NeighborsTest.java`: Pobla un grafo con datos midiendo uso de memoria y tiempo que demora la carga. Luego mide tiempo de obtención de vecinos para un archivo que posee ID de nodos. `NodesNeighborsTest.launch` posee los parámetros.
- `PathFindTest.java`: Pobla un grafo con datos mide el tiempo y uso de memoria de la búsqueda de caminos para grupos de nodos. `PathFindTest.launch` posee los parámetros.



