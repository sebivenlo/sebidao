-- 4 params, two times table name, idname and sequence name
-- @param tablename
-- @param recordname (typically also table  name
-- @param name of the id column
-- @param name of the sequence
-- the prepared statement parameter left is one ?, the json object that comes in.
insert into %s select * from
jsonb_populate_record(null::%s,
    jsonb_set(cast(? as jsonb),
              '{"%s"}'::text[],
               (nextval('%s'::regclass))::text::jsonb)
) returning *
