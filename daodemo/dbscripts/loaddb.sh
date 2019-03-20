#!/bin/bash
db=fluidbusiness
scriptdir=$(dirname $0)
props=${scriptdir}/../connection.properties
if [ -e ${props} ]; then
    source ${scriptdir}/../connection.properties
fi
dropdb --if-exists ${db}
createdb -O exam ${db}
cat ${scriptdir}/schema.sql | psql -X ${db} 
cat ${scriptdir}/newpiet.sql | psql -X ${db} 
