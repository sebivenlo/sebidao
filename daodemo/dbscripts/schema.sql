BEGIN WORK;

CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;

CREATE DOMAIN public.email AS public.citext
	CONSTRAINT email_check CHECK ((VALUE OPERATOR(public.~) '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::public.citext));


CREATE TABLE departments (
       departmentid serial primary key,
       name text,
       description text
) WITHOUT oids;

CREATE TABLE employees (
       employeeid serial primary key,
       lastname text,
       firstname text,
       email email unique not null,
       departmentid integer references departments

) WITHOUT oids;


ALTER DOMAIN public.email OWNER TO exam;
ALTER TABLE public.departments OWNER TO exam;
ALTER TABLE public.employees OWNER TO exam;

COMMIT;
