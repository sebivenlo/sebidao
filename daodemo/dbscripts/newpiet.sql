begin work;

truncate employees cascade;
truncate departments cascade;

insert into departments (name,description,email) values('sales','loud mouths','sales@example.com');
insert into employees (lastname,firstname,email,department)
       values ('Puk','Piet','p.puk@vanderheiden.nl','sales');

commit;
