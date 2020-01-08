#!/bin/bash
scriptdir=$(dirname $0)
cd ${scriptdir}
dropdb --if-exists fantysuniversity 
createdb fantysuniversity -O exam
cat fantysuniversity-dump-9.6.sql  | psql -X fantysuniversity 

