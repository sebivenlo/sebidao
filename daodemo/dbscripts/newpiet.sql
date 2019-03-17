begin work;

truncate employees;
insert into departments (name,description) values('sales','loud mouths');
insert into employees (lastname,firstname,email,departmentid)
values ('Puk','Piet','p.puk@vanderheiden.nl',1);

commit;
