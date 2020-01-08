--begin work;

drop table if exists uppi;
create table uppi (id serial primary key, naam text default 'duppi');
insert into uppi (naam) values(default);
select  * from uppi;

prepare  update_uppi(jsonb)  as 
	 with js as ( select cast($1 as jsonb) j ),
                pk as (select cast(1 as integer) as k ),
		rec as ( select * from jsonb_populate_record(null::uppi, (select j from js)))
--		select * from rec ;
		update  uppi set (id,naam) =( select id,naam from rec) 
                where id=(select k from pk)
		returning *;
                -- returning *;
		
 -- expected returning value and effect on database to be 1, kwartje
execute update_uppi('{"id":1,"naam":"kwartje"}');
--commit;
