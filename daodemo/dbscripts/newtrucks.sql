begin work;
truncate trucks restart identity cascade;
insert into trucks (plate) values('Volvo'),('Vroom');
table trucks;
commit;
