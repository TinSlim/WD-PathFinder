cd frontend
npm run build
cd ../rdf-entity-path
.\mvnw package -DskipTests
cp target/rdf-entity-path-0.0.1-SNAPSHOT.jar ../export/rdf-entity-path-0.0.1-SNAPSHOT.jar
cd ..