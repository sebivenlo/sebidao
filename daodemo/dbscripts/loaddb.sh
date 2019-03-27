#!/bin/bash
db=fluidbusiness
scriptdir=$(dirname $0)
server=localhost
props=${scriptdir}/../connection.properties
if [ -e ${props} ]; then
    source ${scriptdir}/../connection.properties
fi
dropdb -h ${server}  -U postgres --if-exists ${db}  
createdb -h ${server} -U postgres -O ${dbuser} ${db}

cat ${scriptdir}/schema.sql | psql -h ${server} -U postgres -X ${db} 
cat ${scriptdir}/newpiet.sql | psql -h ${server} -U postgres  -X ${db} 
