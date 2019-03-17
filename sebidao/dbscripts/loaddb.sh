#!/bin/bash
dbname=simpledao
scriptdir=$(dirname $0)
dropdb --if-exists ${dbname}
createdb -O exam ${dbname}
cat ${scriptdir}/schema.sql | psql -X ${dbname} 
cat ${scriptdir}/newpiet.sql | psql -X ${dbname} 
