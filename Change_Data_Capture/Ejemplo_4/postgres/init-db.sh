#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  \c loyalty_data;
  \i home/create_schema.sql;
  \i home/seed_data.sql;