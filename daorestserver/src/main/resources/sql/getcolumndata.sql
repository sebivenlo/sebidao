-- @param 1 table name
SELECT DISTINCT
    a.attnum as num,
    a.attname as name,
    format_type(a.atttypid, a.atttypmod) as dtype,
    a.attnotnull as notnull, 
--    com.description as comment,
    coalesce(i.indisprimary,false) as primary_key,
    def.adsrc as default
FROM pg_attribute a 
    JOIN pg_class pgc ON pgc.oid = a.attrelid
    LEFT JOIN pg_index i ON (pgc.oid = i.indrelid AND i.indkey[0] = a.attnum)
    LEFT JOIN pg_attrdef def ON (a.attrelid = def.adrelid AND a.attnum = def.adnum)
WHERE a.attnum > 0 AND pgc.oid = a.attrelid
    AND pg_table_is_visible(pgc.oid)
    AND NOT a.attisdropped
    AND pgc.relname = ?
    ORDER BY a.attnum;
