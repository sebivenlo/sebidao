begin work;

truncate employees;
insert into departments (name,description,email) values('sales','loud mouths','sales@example.com');
insert into employees (lastname,firstname,email,departmentid)
values ('Puk','Piet','p.puk@vanderheiden.nl',1);

commit;
