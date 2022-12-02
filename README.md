# RDF-Path-server

## Lanzar Servidor desde `.jar`

Primero se debe compilar front, luego lanzar el sevidor:

```
cd .\frontend\
npm run prod-build
cd ..\rdf-entity-path\
.\mvnw package
cd .\target\
java -jar .\rdf-entity-path-0.0.1-SNAPSHOT.jar
```

