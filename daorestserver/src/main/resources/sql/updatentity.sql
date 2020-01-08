
begin work;

prepare  update_uppi (jsonb) as 
	 with js as ( select cast($1 as jsonb) j ),
                pk as (select cast(j->>'id' as integer) k from js),
                rec as (select jsonb_populate_record(null::uppi,j) from js)
--		select * from rec
update uppi set(id,naam) = (select id,naam from rec) 
                where id=(select k from pk)
                returning *;

execute update_uppi('{"id":1,"naam":"duppi"}');
commit;
