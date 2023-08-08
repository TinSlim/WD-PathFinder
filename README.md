# WD-PathFinder

This project provides the source code for WoolNet (see [demo](https://woolnet.dcc.uchile.cl/)): a visual system to help explore connections and thematic sub-graphs for Wikidata.

## Requirements

- Node: v16.17.1
- Java: openjdk 11

## Preparation

- In `/frontend` call `npm i` to install dependenciaes.
- Load `/rdf-entity-path` into **Eclipse IDE**.
- To create data to populate the graph, see the folder `python`.
- To create test data, see the folder `python`.

### Launch Production

- Set in `/frontend/.env` the following environment variables as correspond to your setting.

```
BASE_URL = "https://urldeejemplo.ejemplo"		# Url of the application
WEB_SOCKET_URL = "wss://urldeejemplo.ejemplo"	# Url of the WebSocket
```

- Run `build.ps1`.
- The file that runs the application exports to `/export`.
- Run the file with the following parameters:
	- `-Xmx59g` : RAM available.
	- `-Dgraph-data=` : Pass the parameter the path to the file with graph data.

For example:

```
java -jar -Xmx59g -Dgraph-data="latest-truthy_small" rdf-entity-path-0.0.1-SNAPSHOT.jar
```

> The data format used should be adjacency (Adyacente); if not, you must prepare the data in this format.

### Launch Locally for Development

- Set in `/frontend/.env` the following environment variables.

```
BASE_URL = "http://localhost:8080"
WEB_SOCKET_URL = "ws://localhost:8080"
```

- Run the following commands:

```
cd frontend
npm run dev
```

- The file `frontend/public/index.html` will show the live changes.

- Run the following commands:

```
npm run build
```

- Use `RunServer.launch` to launch 

### Tests

The classes found in `rdf-entity-path/src/main/experiments` can be used for experiments/testing and should be compiled to a `.jar`.

- `NeighborsTest.java`: Loads a graph measuring memory and time usage. Later it measures the time needed to obtain the neighbours of a particular node. `NodesNeighborsTest.launch` contains the parameters.
- `PathFindTest.java`: Loads a graph and measures the memory and time usage for finding paths that connect two or more nodes. `PathFindTest.launch` contains the parameters.



