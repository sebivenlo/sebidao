--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3 (Ubuntu 10.3-1.pgdg16.04+1)
-- Dumped by pg_dump version 10.3 (Ubuntu 10.3-1.pgdg16.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: citext; Type: EXTENSION; Schema: -; Owner: 
--

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


ALTER DOMAIN public.email OWNER TO hom;

--
-- Name: gender; Type: TYPE; Schema: public; Owner: hom
--

CREATE TYPE public.gender AS ENUM (
    'F',
    'M',
    'U'
);


ALTER TYPE public.gender OWNER TO hom;

--
-- Name: myrow; Type: TYPE; Schema: public; Owner: hom
--

CREATE TYPE public.myrow AS (
	snummer integer,
	grade numeric
);


ALTER TYPE public.myrow OWNER TO hom;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: exam_event; Type: TABLE; Schema: public; Owner: hom
--

CREATE TABLE public.exam_event (
    exam_event_id integer NOT NULL,
    name text NOT NULL,
    event_date date NOT NULL,
    teachers text[] NOT NULL,
    CONSTRAINT exam_event_teachers_check CHECK ((array_length(teachers, 1) > 0))
);


ALTER TABLE public.exam_event OWNER TO hom;

--
-- Name: exam_event_id_seq; Type: SEQUENCE; Schema: public; Owner: hom
--

CREATE SEQUENCE public.exam_event_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exam_event_id_seq OWNER TO hom;

--
-- Name: exam_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: hom
--

ALTER SEQUENCE public.exam_event_id_seq OWNED BY public.exam_event.exam_event_id;


--
-- Name: exam_results; Type: TABLE; Schema: public; Owner: hom
--

CREATE TABLE public.exam_results (
    id integer NOT NULL,
    exam_event_id integer NOT NULL,
    snummer integer NOT NULL,
    grade numeric NOT NULL
);


ALTER TABLE public.exam_results OWNER TO hom;

--
-- Name: exam_result_id_seq; Type: SEQUENCE; Schema: public; Owner: hom
--

CREATE SEQUENCE public.exam_result_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exam_result_id_seq OWNER TO hom;

--
-- Name: exam_result_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: hom
--

ALTER SEQUENCE public.exam_result_id_seq OWNED BY public.exam_results.id;


--
-- Name: sresult; Type: TABLE; Schema: public; Owner: hom
--

CREATE TABLE public.sresult (
    snummer integer,
    grade numeric
);


ALTER TABLE public.sresult OWNER TO hom;

--
-- Name: students_id_seq; Type: SEQUENCE; Schema: public; Owner: hom
--

CREATE SEQUENCE public.students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.students_id_seq OWNER TO hom;

--
-- Name: students; Type: TABLE; Schema: public; Owner: hom
--

CREATE TABLE public.students (
    snummer integer DEFAULT nextval('public.students_id_seq'::regclass) NOT NULL,
    lastname text NOT NULL,
    firstname text NOT NULL,
    tussenvoegsel text,
    email public.email NOT NULL,
    cohort integer NOT NULL,
    student_class character varying(4) NOT NULL,
    gender public.gender NOT NULL,
    dob date NOT NULL
);


ALTER TABLE public.students OWNER TO hom;

--
-- Name: exam_event exam_event_id; Type: DEFAULT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.exam_event ALTER COLUMN exam_event_id SET DEFAULT nextval('public.exam_event_id_seq'::regclass);


--
-- Name: exam_results id; Type: DEFAULT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.exam_results ALTER COLUMN id SET DEFAULT nextval('public.exam_result_id_seq'::regclass);


--
-- Name: exam_event exam_event_pkey; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.exam_event
    ADD CONSTRAINT exam_event_pkey PRIMARY KEY (exam_event_id);


--
-- Name: exam_results exam_result_pkey; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.exam_results
    ADD CONSTRAINT exam_result_pkey PRIMARY KEY (id);


--
-- Name: exam_results exam_results_cand_grade_un; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.exam_results
    ADD CONSTRAINT exam_results_cand_grade_un UNIQUE (exam_event_id, snummer);


--
-- Name: students unseen_email_un; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT unseen_email_un UNIQUE (email);


--
-- Name: students unseenstudents_pk; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT unseenstudents_pk PRIMARY KEY (snummer);


--
-- Name: exam_results exam_result_exam_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.exam_results
    ADD CONSTRAINT exam_result_exam_event_id_fkey FOREIGN KEY (exam_event_id) REFERENCES public.exam_event(exam_event_id);


--
-- PostgreSQL database dump complete
--

