begin work;

truncate employees restart identity;
truncate departments restart identity cascade;
truncate companies restart identity;
truncate truckplans restart identity cascade;
truncate trucks restart identity cascade;

-- alter sequence employees_employeeid_seq restart;
-- alter sequence departments_departmentid_seq restart;
-- alter sequence truckplans_truckplanid_seq restart;
-- alter sequence trucks_truckid_seq restart;

insert into departments (name,description,email)
       values('sales','loud mouths','info@example.com');

insert into employees (lastname,firstname,email,departmentid)
values ('Puk','Piet','p.puk@vanderheiden.nl',1);

insert into trucks (plate) values( 'Vroooom');

insert into truckplans(truckid,plan) values(1,'[2019-05-05T19:30,2019-05-06T8:30)');

commit;

table employees;
table departments;
table companies;
table trucks;
table truckplans;

