begin work;

truncate employees;
truncate departments cascade;

alter sequence employees_employeeid_seq restart;
alter sequence departments_departmentid_seq restart;
insert into departments (name,description,email)
       values('sales','loud mouths','info@example.com');

insert into employees (lastname,firstname,email,departmentid)
values ('Puk','Piet','p.puk@vanderheiden.nl',1);

commit;

table employees;
table departments;
