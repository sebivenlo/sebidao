begin work;

truncate employees;
truncate departments cascade;

alter sequence employees_employeeid_seq restart;
alter sequence departments_departmentid_seq restart;
insert into departments (name,description) values('sales','loud mouths');

insert into employees (lastname,firstname,email,departmentid)
values ('Puk','Piet','p.puk@vanderheiden.nl',1);

commit;
