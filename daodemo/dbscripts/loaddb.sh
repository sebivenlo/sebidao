#!/bin/bash
db=fluidbusiness
scriptdir=$(dirname $0)
server=localhost
props=${scriptdir}/../connection.properties
ADMINUSER=
if [ -e ${props} ]; then
    source ${scriptdir}/../connection.properties
fi
dropdb -h ${server}  ${ADMINUSER}  --if-exists ${db}  
createdb -h ${server} ${ADMINUSER}  -O ${dbuser} ${db}

cat ${scriptdir}/schema.sql | psql -h ${server} ${ADMINUSER}  -X ${db} 
cat ${scriptdir}/newpiet.sql | psql -h ${server} ${ADMINUSER}   -X ${db} 
cat ${scriptdir}/newtrucks.sql | psql -h ${server} ${ADMINUSER}   -X ${db} 

