FROM debezium/postgres:latest

COPY ./create_schema.sql /home/create_schema.sql
COPY ./seed_data.sql /home/seed_data.sql
COPY ./init-db.sh /docker-entrypoint-initdb.d/init-db.sh