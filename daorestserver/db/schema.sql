begin work;

CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;


--
-- Name: EXTENSION citext; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION citext IS 'data type for case-insensitive character strings';


--
-- Name: email; Type: DOMAIN; Schema: public; Owner: hom
--

CREATE DOMAIN public.email AS public.citext
	CONSTRAINT email_check CHECK ((VALUE OPERATOR(public.~) '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::public.citext));


COMMENT ON DOMAIN public.email is 'validating email addresses and case insensitive uniqueness check';
--
-- Name: gender; Type: TYPE; Schema: public; Owner: hom
--

CREATE TYPE public.gender AS ENUM (
    'F',
    'M',
    'U'
);

CREATE DOMAIN public.abrev as char(3) constraint abrev_check CHECK (value::text ~ '^[A-Z]{3}$');
SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: unseenstudents; Type: TABLE; Schema: public; Owner: hom
--

CREATE TABLE public.students (
    snummer serial PRIMARY KEY,
    firstname text NOT NULL,
    lastname text NOT NULL,
    tussenvoegsel text,
    email public.email NOT NULL UNIQUE,
    cohort integer NOT NULL,
    student_class character varying(4) NOT NULL,
    gender public.gender NOT NULL,
    dob date NOT NULL
);

create table teachers (
       teacher_id serial primary key,
       abreviation abrev not null unique,
       teacher_name TEXT not null
);

CREATE TABLE public.courses (
       course_id SERIAL PRIMARY KEY,
       course_credits smallint NOT NULL DEFAULT 5,
       course_name text UNIQUE NOT NULL ,
       course_description text NOT NULL,
       course_teachers text[] NOT NULL CHECK ((array_length(course_teachers, 1) > 0))
);

CREATE TABLE public.exam_events (
    exam_event_id SERIAL PRIMARY KEY,
    event_name text NOT NULL,
    event_date date NOT NULL,
    examiners text[] NOT NULL  CHECK ((array_length(examiners, 1) > 0)),
     UNIQUE(event_name,event_date)
);

CREATE TABLE public.exam_results (
    exam_result_id SERIAL PRIMARY KEY,
    exam_event_id integer NOT NULL references exam_events(exam_event_id) on delete cascade,
    snummer integer NOT NULL references students(snummer) on delete cascade,
    grade numeric NOT NULL CHECK (grade between 1.0 and 10.0)
);


commit;
