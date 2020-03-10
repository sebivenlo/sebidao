BEGIN WORK;

CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE DOMAIN public.email AS public.citext
	CONSTRAINT email_check CHECK ((VALUE OPERATOR(public.~) '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::public.citext));


CREATE TABLE departments (
       name text,
       description text,
       email text not null unique,
       department_id serial primary key
) WITHOUT oids;

CREATE TABLE employees (
       employee_id serial primary key,
       lastname text,
       firstname text,
       email text unique not null,
       department_id integer references departments,
       available boolean,
       dob date not null default now()

) WITHOUT oids;

CREATE TABLE companies (
        name text ,
        country text,
        city text,
        address text,
        ticker text primary key,
        postcode text,
	someint integer,
	someinteger integer
) WITHOUT oids;

create table trucks (
       truck_id serial primary key,
       plate text
);

create table truckplans(
       truckplan_id serial primary key,
       truck_id integer references trucks,
       plan tsrange,
       exclude using gist ( truck_id with =, plan with &&)
);


ALTER DOMAIN public.email OWNER TO exam;
ALTER TABLE public.departments OWNER TO exam;
ALTER TABLE public.employees OWNER TO exam;
ALTER TABLE public.companies OWNER TO exam;
ALTER TABLE public.trucks OWNER TO exam;
ALTER TABLE public.truckplans OWNER TO exam;

COMMIT;
