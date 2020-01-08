begin work;

drop table if exists uppi;
create table uppi (id serial primary key, naam text default 'uppi');
insert into uppi (naam) values(default);
select  * from uppi;

prepare  update_uppi  as 
	 with js as ( select cast($1 as jsonb) j ),
                pk as (select cast(1 as integer) as k )
	--	select * from rec r;
		update  uppi set (id,naam) =( select cast(j->>'id' as integer), cast( j->>'naam' as text) from js)
                where id=(select k from pk)
		returning *;
                -- returning *;
		

execute update_uppi('{"id":1,"naam":"dopi"}');
commit;
