--
-- Author:  Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
-- Created: May 10, 2018
-- 

-- @stringparam primary key name
-- @stringparam table name
-- @stringparam table name
-- @stringparam column names to update
-- @stringparam column names to update
-- @stringparam primary key name
-- @queryparam 1 jsonb string

with js as ( select cast(? as jsonb) j ),
     pk as (select cast(j->>'%s' as integer) as k from js),
     rec as ( select * from jsonb_populate_record(null::%s, (select j from js)))
update  %s 
   set (%s) = (select %s from rec)
where %s=(select k from pk)
     returning *