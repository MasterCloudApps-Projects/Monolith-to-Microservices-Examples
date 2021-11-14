# setup connectors
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @Ejemplo_4/pg-connectors/pg_loyalty_data-source.json
