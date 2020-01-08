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
-- Name: abrev; Type: DOMAIN; Schema: public; Owner: exam
--

CREATE DOMAIN public.abrev AS character(3)
	CONSTRAINT abrev_check CHECK (((VALUE)::text ~ '^[A-Z]{3}$'::text));


ALTER DOMAIN public.abrev OWNER TO exam;

--
-- Name: email; Type: DOMAIN; Schema: public; Owner: exam
--

CREATE DOMAIN public.email AS public.citext
	CONSTRAINT email_check CHECK ((VALUE OPERATOR(public.~) '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::public.citext));


ALTER DOMAIN public.email OWNER TO exam;

--
-- Name: DOMAIN email; Type: COMMENT; Schema: public; Owner: exam
--

COMMENT ON DOMAIN public.email IS 'validating email addresses and case insensitive uniqueness check';


--
-- Name: gender; Type: TYPE; Schema: public; Owner: exam
--

CREATE TYPE public.gender AS ENUM (
    'F',
    'M',
    'U'
);


ALTER TYPE public.gender OWNER TO exam;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: exam_events; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.exam_events (
    exam_event_id integer NOT NULL,
    event_name text NOT NULL,
    event_date date NOT NULL,
    examiners text[] NOT NULL,
    CONSTRAINT exam_events_examiners_check CHECK ((array_length(examiners, 1) > 0))
);


ALTER TABLE public.exam_events OWNER TO exam;

--
-- Name: exam_results; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.exam_results (
    exam_result_id integer NOT NULL,
    exam_event_id integer NOT NULL,
    snummer integer NOT NULL,
    grade numeric NOT NULL,
    CONSTRAINT exam_results_grade_check CHECK (((grade >= 1.0) AND (grade <= 10.0)))
);


ALTER TABLE public.exam_results OWNER TO exam;

--
-- Name: students; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.students (
    snummer integer NOT NULL,
    firstname text NOT NULL,
    lastname text NOT NULL,
    tussenvoegsel text,
    email public.email NOT NULL,
    cohort integer NOT NULL,
    student_class character varying(4) NOT NULL,
    gender public.gender NOT NULL,
    dob date NOT NULL
);


ALTER TABLE public.students OWNER TO exam;

--
-- Name: COLUMN students.dob; Type: COMMENT; Schema: public; Owner: exam
--

COMMENT ON COLUMN public.students.dob IS 'Date of Birth';


--
-- Name: comprehensive_exam_results; Type: VIEW; Schema: public; Owner: exam
--

CREATE VIEW public.comprehensive_exam_results AS
 SELECT students.snummer,
    students.lastname,
    students.firstname,
    students.tussenvoegsel,
    students.email,
    students.student_class,
    exam_events.event_name,
    exam_events.event_date,
    exam_events.examiners,
    exam_results.grade,
    exam_results.exam_result_id
   FROM ((public.students
     JOIN public.exam_results USING (snummer))
     JOIN public.exam_events USING (exam_event_id));


ALTER TABLE public.comprehensive_exam_results OWNER TO exam;

--
-- Name: courses; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.courses (
    course_id integer NOT NULL,
    course_credits smallint DEFAULT 5 NOT NULL,
    course_name text NOT NULL,
    course_description text NOT NULL,
    course_teachers text[] NOT NULL,
    CONSTRAINT courses_course_teachers_check CHECK ((array_length(course_teachers, 1) > 0))
);


ALTER TABLE public.courses OWNER TO exam;

--
-- Name: courses_course_id_seq; Type: SEQUENCE; Schema: public; Owner: exam
--

CREATE SEQUENCE public.courses_course_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.courses_course_id_seq OWNER TO exam;

--
-- Name: courses_course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: exam
--

ALTER SEQUENCE public.courses_course_id_seq OWNED BY public.courses.course_id;


--
-- Name: exam_events_exam_event_id_seq; Type: SEQUENCE; Schema: public; Owner: exam
--

CREATE SEQUENCE public.exam_events_exam_event_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exam_events_exam_event_id_seq OWNER TO exam;

--
-- Name: exam_events_exam_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: exam
--

ALTER SEQUENCE public.exam_events_exam_event_id_seq OWNED BY public.exam_events.exam_event_id;


--
-- Name: exam_results_exam_result_id_seq; Type: SEQUENCE; Schema: public; Owner: exam
--

CREATE SEQUENCE public.exam_results_exam_result_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.exam_results_exam_result_id_seq OWNER TO exam;

--
-- Name: exam_results_exam_result_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: exam
--

ALTER SEQUENCE public.exam_results_exam_result_id_seq OWNED BY public.exam_results.exam_result_id;


--
-- Name: sresult; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.sresult (
    snummer integer,
    grade numeric
);


ALTER TABLE public.sresult OWNER TO exam;

--
-- Name: TABLE sresult; Type: COMMENT; Schema: public; Owner: exam
--

COMMENT ON TABLE public.sresult IS 'composite type for grade filtering';


--
-- Name: students_snummer_seq; Type: SEQUENCE; Schema: public; Owner: exam
--

CREATE SEQUENCE public.students_snummer_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.students_snummer_seq OWNER TO exam;

--
-- Name: students_snummer_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: exam
--

ALTER SEQUENCE public.students_snummer_seq OWNED BY public.students.snummer;


--
-- Name: teachers; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.teachers (
    teacher_id integer NOT NULL,
    abreviation public.abrev NOT NULL,
    teacher_name text NOT NULL
);


ALTER TABLE public.teachers OWNER TO exam;

--
-- Name: teachers_teacher_id_seq; Type: SEQUENCE; Schema: public; Owner: exam
--

CREATE SEQUENCE public.teachers_teacher_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teachers_teacher_id_seq OWNER TO exam;

--
-- Name: teachers_teacher_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: exam
--

ALTER SEQUENCE public.teachers_teacher_id_seq OWNED BY public.teachers.teacher_id;


--
-- Name: uppi; Type: TABLE; Schema: public; Owner: exam
--

CREATE TABLE public.uppi (
    id integer NOT NULL,
    naam text DEFAULT 'duppi'::text
);


ALTER TABLE public.uppi OWNER TO exam;

--
-- Name: uppi_id_seq; Type: SEQUENCE; Schema: public; Owner: exam
--

CREATE SEQUENCE public.uppi_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.uppi_id_seq OWNER TO exam;

--
-- Name: uppi_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: exam
--

ALTER SEQUENCE public.uppi_id_seq OWNED BY public.uppi.id;


--
-- Name: courses course_id; Type: DEFAULT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.courses ALTER COLUMN course_id SET DEFAULT nextval('public.courses_course_id_seq'::regclass);


--
-- Name: exam_events exam_event_id; Type: DEFAULT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_events ALTER COLUMN exam_event_id SET DEFAULT nextval('public.exam_events_exam_event_id_seq'::regclass);


--
-- Name: exam_results exam_result_id; Type: DEFAULT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_results ALTER COLUMN exam_result_id SET DEFAULT nextval('public.exam_results_exam_result_id_seq'::regclass);


--
-- Name: students snummer; Type: DEFAULT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.students ALTER COLUMN snummer SET DEFAULT nextval('public.students_snummer_seq'::regclass);


--
-- Name: teachers teacher_id; Type: DEFAULT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.teachers ALTER COLUMN teacher_id SET DEFAULT nextval('public.teachers_teacher_id_seq'::regclass);


--
-- Name: uppi id; Type: DEFAULT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.uppi ALTER COLUMN id SET DEFAULT nextval('public.uppi_id_seq'::regclass);


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: exam
--

INSERT INTO public.courses VALUES (4, 5, 'DVP', 'Development Processes', '{GRE,MAS}');
INSERT INTO public.courses VALUES (1, 5, 'PRC2', 'Programming Concepts 2', '{BIG,GRE}');
INSERT INTO public.courses VALUES (2, 5, 'PRJ2', 'Logistics Rest Application', '{BIG,FUL,GRE,MAS,WID}');
INSERT INTO public.courses VALUES (3, 5, 'DBS', 'Database systems RDMBS', '{BIG,WID}');


--
-- Data for Name: exam_events; Type: TABLE DATA; Schema: public; Owner: exam
--

INSERT INTO public.exam_events VALUES (1, 'PRC2', '2018-05-03', '{ham,him}');
INSERT INTO public.exam_events VALUES (3, 'PRC2', '2018-09-23', '{HOM,HVD,SOB}');


--
-- Data for Name: exam_results; Type: TABLE DATA; Schema: public; Owner: exam
--

INSERT INTO public.exam_results VALUES (1, 3, 3640497, 2.5);
INSERT INTO public.exam_results VALUES (2, 3, 3640447, 6.6);
INSERT INTO public.exam_results VALUES (3, 3, 3640446, 7.5);
INSERT INTO public.exam_results VALUES (4, 3, 3690803, 1.0);
INSERT INTO public.exam_results VALUES (5, 3, 3640444, 3.2);
INSERT INTO public.exam_results VALUES (6, 3, 3640443, 7.1);
INSERT INTO public.exam_results VALUES (7, 3, 3640442, 2.4);
INSERT INTO public.exam_results VALUES (8, 3, 3640441, 3.4);
INSERT INTO public.exam_results VALUES (9, 3, 3640487, 7.8);
INSERT INTO public.exam_results VALUES (10, 3, 3690794, 8.5);
INSERT INTO public.exam_results VALUES (11, 3, 3640485, 9.9);
INSERT INTO public.exam_results VALUES (12, 3, 3690795, 8.1);
INSERT INTO public.exam_results VALUES (13, 3, 3640483, 5.0);
INSERT INTO public.exam_results VALUES (14, 3, 3640481, 9.9);
INSERT INTO public.exam_results VALUES (15, 3, 3640494, 1.0);
INSERT INTO public.exam_results VALUES (16, 3, 3640491, 7.9);
INSERT INTO public.exam_results VALUES (17, 3, 3640490, 3.7);
INSERT INTO public.exam_results VALUES (18, 3, 3640488, 6.0);
INSERT INTO public.exam_results VALUES (19, 3, 3640470, 7.2);
INSERT INTO public.exam_results VALUES (20, 3, 3640468, 1.0);
INSERT INTO public.exam_results VALUES (21, 3, 3640465, 2.5);
INSERT INTO public.exam_results VALUES (22, 3, 3640464, 1.0);
INSERT INTO public.exam_results VALUES (23, 3, 3640478, 4.7);
INSERT INTO public.exam_results VALUES (24, 3, 3640477, 3.6);
INSERT INTO public.exam_results VALUES (25, 3, 3640475, 8.9);
INSERT INTO public.exam_results VALUES (26, 3, 3640474, 1.9);
INSERT INTO public.exam_results VALUES (27, 3, 3640473, 9.9);
INSERT INTO public.exam_results VALUES (28, 3, 3640472, 7.8);
INSERT INTO public.exam_results VALUES (29, 3, 3640453, 3.6);
INSERT INTO public.exam_results VALUES (30, 3, 3640451, 6.2);
INSERT INTO public.exam_results VALUES (31, 3, 3640449, 10.0);
INSERT INTO public.exam_results VALUES (32, 3, 3640448, 6.3);
INSERT INTO public.exam_results VALUES (33, 3, 3640463, 9.6);
INSERT INTO public.exam_results VALUES (34, 3, 3640462, 7.3);
INSERT INTO public.exam_results VALUES (35, 3, 3640461, 1.8);
INSERT INTO public.exam_results VALUES (36, 3, 3640460, 8.3);
INSERT INTO public.exam_results VALUES (37, 3, 3640459, 1.0);
INSERT INTO public.exam_results VALUES (38, 3, 3640458, 8.6);
INSERT INTO public.exam_results VALUES (39, 3, 3640456, 3.6);


--
-- Data for Name: sresult; Type: TABLE DATA; Schema: public; Owner: exam
--



--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: exam
--

INSERT INTO public.students VALUES (3640441, 'Bebe', 'Agena', NULL, 'bebeagena@student.olifantys.nl', 2021, 'DKAB', 'F', '1998-11-06');
INSERT INTO public.students VALUES (3640442, 'Arlinda', 'Alldredge', NULL, 'arlindaalldredge@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-02-24');
INSERT INTO public.students VALUES (3640443, 'Sid', 'Basta', NULL, 'sidbasta@student.olifantys.nl', 2021, 'DKAA', 'M', '1994-10-17');
INSERT INTO public.students VALUES (3640444, 'Genny', 'Berg', 'van den', 'gennyvandenberg@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-08-11');
INSERT INTO public.students VALUES (3640445, 'Ariel', 'Bodden', NULL, 'arielbodden@student.olifantys.nl', 2021, 'DKAC', 'F', '1995-11-13');
INSERT INTO public.students VALUES (3640446, 'Pierre', 'Boese', NULL, 'pierreboese@student.olifantys.nl', 2019, 'DKAB', 'M', '1997-03-03');
INSERT INTO public.students VALUES (3640447, 'Elin', 'Boggs', NULL, 'elinboggs@student.olifantys.nl', 2021, 'DKAC', 'F', '1998-06-05');
INSERT INTO public.students VALUES (3640448, 'Herschel', 'Buford', NULL, 'herschelbuford@student.olifantys.nl', 2021, 'DKAB', 'M', '1997-12-09');
INSERT INTO public.students VALUES (3640449, 'John', 'Caro', NULL, 'johncaro@student.olifantys.nl', 2021, 'DKAC', 'M', '1998-07-06');
INSERT INTO public.students VALUES (3640450, 'Shella', 'Clifton', NULL, 'shellaclifton@student.olifantys.nl', 2021, 'DKAB', 'F', '1996-12-13');
INSERT INTO public.students VALUES (3640451, 'Lenore', 'Coomes', NULL, 'lenorecoomes@student.olifantys.n', 2021, 'DKAA', 'F', '1994-06-20');
INSERT INTO public.students VALUES (3640452, 'Milagros', 'Costillo', NULL, 'milagroscostillo@student.olifantys.nl', 2021, 'DKAC', 'M', '1998-07-08');
INSERT INTO public.students VALUES (3640453, 'Horace', 'Crofoot', NULL, 'horacecrofoot@student.olifantys.nl', 2021, 'DKAA', 'M', '1998-05-27');
INSERT INTO public.students VALUES (3640454, 'Nathan', 'Dudding', NULL, 'nathandudding@student.olifantys.nl', 2021, 'DKAC', 'M', '1998-03-19');
INSERT INTO public.students VALUES (3640455, 'Ezequiel', 'Fairweather', NULL, 'ezequielfairweather@student.olifantys.nl', 2021, 'DKAB', 'M', '1998-06-28');
INSERT INTO public.students VALUES (3640456, 'Judson', 'Fishel', NULL, 'judsonfishel@student.olifantys.nl', 2021, 'DKAA', 'M', '1997-03-11');
INSERT INTO public.students VALUES (3640458, 'Jennie', 'Foerster', NULL, 'jenniefoerster@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-12-15');
INSERT INTO public.students VALUES (3640459, 'Dexter', 'Follmer', NULL, 'dexterfollmer@student.olifantys.nl', 2021, 'DKAC', 'M', '1994-07-27');
INSERT INTO public.students VALUES (3640460, 'Clair', 'Fuselier', NULL, 'clairfuselier@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-04-12');
INSERT INTO public.students VALUES (3640461, 'Ulrike', 'Garretson', NULL, 'ulrikegarretson@student.olifantys.nl', 2021, 'DKAB', 'F', '1994-06-23');
INSERT INTO public.students VALUES (3640462, 'Billy', 'Gerst', NULL, 'billygerst@student.olifantys.nl', 2021, 'DKAB', 'M', '1994-04-05');
INSERT INTO public.students VALUES (3640463, 'Marg', 'Gillman', NULL, 'marggillman@student.olifantys.nl', 2021, 'DKAA', 'F', '1994-02-09');
INSERT INTO public.students VALUES (3640464, 'Onita', 'Gloss', NULL, 'onitagloss@student.olifantys.nl', 2021, 'DKAB', 'F', '1994-12-06');
INSERT INTO public.students VALUES (3640465, 'Josie', 'Gramling', NULL, 'josiegramling@student.olifantys.nl', 2021, 'DKAA', 'F', '1996-05-18');
INSERT INTO public.students VALUES (3640466, 'Mark', 'Hodak', 'van der', 'markhodak@student.olifantys.nl', 2021, 'DKAA', 'F', '1997-11-08');
INSERT INTO public.students VALUES (3640467, 'Barney', 'Holz', 'to', 'barneyholz@student.olifantys.nl', 2021, 'DKAC', 'M', '1995-07-25');
INSERT INTO public.students VALUES (3640468, 'Glinda', 'Joines', NULL, 'glindajoines@student.olifantys.nl', 2021, 'DKAB', 'F', '1996-09-11');
INSERT INTO public.students VALUES (3640469, 'Talitha', 'Link', NULL, 'talithalink@student.olifantys.nl', 2021, 'DKAA', 'F', '1998-01-14');
INSERT INTO public.students VALUES (3640470, 'Inez', 'Lukach', NULL, 'inezlukach@student.olifantys.nl', 2021, 'DKAA', 'F', '1995-04-10');
INSERT INTO public.students VALUES (3640471, 'Oma', 'Mcinnis', 'van', 'omamcinnis@student.olifantys.nl', 2021, 'DKAB', 'M', '1997-05-02');
INSERT INTO public.students VALUES (3640472, 'Edie', 'Mckinnon', 'op de', 'ediemckinnon@student.olifantys.nl', 2021, 'DKAB', 'M', '1998-08-14');
INSERT INTO public.students VALUES (3640473, 'Codi', 'Mcspadden', NULL, 'codimcspadden@student.olifantys.nl', 2021, 'DKAC', 'M', '1996-10-05');
INSERT INTO public.students VALUES (3640474, 'Hortencia', 'Melendrez', NULL, 'hortenciamelendrez@student.olifantys.nl', 2021, 'DKAB', 'F', '1994-12-21');
INSERT INTO public.students VALUES (3640475, 'Luba', 'Mendoza', NULL, 'lubamendoza@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-09-17');
INSERT INTO public.students VALUES (3640476, 'Cathie', 'Minor', NULL, 'cathieminor@student.olifantys.nl', 2021, 'DKAA', 'F', '1997-02-14');
INSERT INTO public.students VALUES (3640477, 'Elijah', 'Moench', NULL, 'elijahmoench@student.olifantys.nl', 2021, 'DKAC', 'M', '1995-01-25');
INSERT INTO public.students VALUES (3640478, 'Lauran', 'Moncayo', NULL, 'lauranmoncayo@student.olifantys.nl', 2021, 'DKAA', 'M', '1995-02-20');
INSERT INTO public.students VALUES (3640479, 'Brigida', 'Mowbray', NULL, 'brigidamowbray@student.olifantys.nl', 2021, 'DKAA', 'F', '1995-03-24');
INSERT INTO public.students VALUES (3640480, 'Chanell', 'Musselwhite', NULL, 'chanellmusselwhite@student.olifantys.nl', 2021, 'DKAB', 'F', '1994-07-23');
INSERT INTO public.students VALUES (3640481, 'Myriam', 'Nanney', NULL, 'myriamnanney@student.olifantys.nl', 2021, 'DKAA', 'F', '1994-07-06');
INSERT INTO public.students VALUES (3640482, 'Socorro', 'Negrin', NULL, 'socorronegrin@student.olifantys.nl', 2021, 'DKAC', 'M', '1995-01-09');
INSERT INTO public.students VALUES (3640483, 'Jolie', 'Neu', NULL, 'jolieneu@student.olifantys.nl', 2019, 'DKAC', 'F', '1995-01-16');
INSERT INTO public.students VALUES (3640484, 'Theola', 'Occhipinti', NULL, 'theolaocchipinti@student.olifantys.nl', 2021, 'DKAB', 'F', '1997-07-23');
INSERT INTO public.students VALUES (3640485, 'Claire', 'Otero', NULL, 'claireotero@student.olifantys.nl', 2021, 'DKAB', 'F', '1995-02-10');
INSERT INTO public.students VALUES (3640486, 'Jeremy', 'Philyaw', NULL, 'jeremyphilyaw@student.olifantys.nl', 2021, 'DKAC', 'M', '1996-01-25');
INSERT INTO public.students VALUES (3640487, 'Monnie', 'Pipkin', NULL, 'monniepipkin@student.olifantys.nl', 2021, 'DKAC', 'F', '1997-05-14');
INSERT INTO public.students VALUES (3640488, 'Daniele', 'Podesta', NULL, 'danielepodesta@student.olifantys.nl', 2021, 'DKAB', 'F', '1995-03-30');
INSERT INTO public.students VALUES (3640489, 'Tomika', 'Printz', NULL, 'tomikaprintz@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-06-10');
INSERT INTO public.students VALUES (3640490, 'Leilani', 'Quirion', NULL, 'leilaniquirion@student.olifantys.nl', 2023, 'DKDD', 'F', '1995-09-05');
INSERT INTO public.students VALUES (3640491, 'Felton', 'Rael', NULL, 'feltonrael@student.olifantys.nl', 2022, 'DKAB', 'M', '1998-09-23');
INSERT INTO public.students VALUES (3640492, 'Sylvia', 'Rahaim', NULL, 'sylviarahaim@student.olifantys.nl', 2021, 'DKAB', 'F', '1998-02-28');
INSERT INTO public.students VALUES (3640493, 'Georgiana', 'Reinhart', NULL, 'georgianareinhart@student.olifantys.nl', 2021, 'DKAC', 'F', '1997-03-17');
INSERT INTO public.students VALUES (3640494, 'Jonell', 'Runion', NULL, 'jonellrunion@student.olifantys.nl', 2021, 'DKAA', 'M', '1995-01-13');
INSERT INTO public.students VALUES (3640495, 'Annette', 'Schaaf', NULL, 'annetteschaaf@student.olifantys.nl', 2021, 'DKAB', 'F', '1998-09-27');
INSERT INTO public.students VALUES (3640496, 'Shanell', 'Spina', NULL, 'shanellspina@student.olifantys.nl', 2019, 'DKAB', 'F', '1997-02-22');
INSERT INTO public.students VALUES (3640497, 'Mahalia', 'Stee', NULL, 'mahaliastee@student.olifantys.nl', 2021, 'DKAC', 'F', '1998-05-28');
INSERT INTO public.students VALUES (3640498, 'Zora', 'Testa', NULL, 'zoratesta@student.olifantys.nl', 2021, 'DKAB', 'F', '1995-10-21');
INSERT INTO public.students VALUES (3640499, 'Georgeanna', 'Tolley', NULL, 'georgeannatolley@student.olifantys.nl', 2021, 'DKAA', 'F', '1995-10-05');
INSERT INTO public.students VALUES (3690793, 'Anitra', 'Troop', NULL, 'anitratroop@student.olifantys.nl', 2020, 'TKDD', 'F', '1997-08-10');
INSERT INTO public.students VALUES (3690794, 'Millicent', 'Uzzle', NULL, 'millicentuzzle@student.olifantys.nl', 2021, 'DKAC', 'F', '1994-12-10');
INSERT INTO public.students VALUES (3690795, 'Royce', 'Wafford', NULL, 'roycewafford@student.olifantys.nl', 2021, 'DKAA', 'M', '1996-08-01');
INSERT INTO public.students VALUES (3690796, 'Harriette', 'Weiner', NULL, 'harrietteweiner@student.olifantys.nl', 2021, 'DKAC', 'F', '1995-04-28');
INSERT INTO public.students VALUES (3690797, 'Shanta', 'Wiemann', NULL, 'shantawiemann@student.olifantys.nl', 2021, 'DKAA', 'F', '1998-10-13');
INSERT INTO public.students VALUES (3690798, 'Fawn', 'Wier', NULL, 'fawnwier@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-08-15');
INSERT INTO public.students VALUES (3690799, 'Yoshiko', 'Wordlaw', NULL, 'yoshikowordlaw@student.olifantys.nl', 2021, 'DKAC', 'F', '1996-05-04');
INSERT INTO public.students VALUES (3690800, 'Janett', 'Xie', NULL, 'janettxie@student.olifantys.nl', 2021, 'DKAC', 'F', '1997-09-30');
INSERT INTO public.students VALUES (3690801, 'Mikki', 'Müller-Yang', NULL, 'mikkiyang@student.olifantys.nl', 2021, 'DKAA', 'M', '1997-03-07');
INSERT INTO public.students VALUES (3690802, 'Shelia', 'Yazzieß', NULL, 'sheliayazzies@student.olifantys.nl', 2021, 'DKAA', 'F', '1996-10-11');
INSERT INTO public.students VALUES (3690803, 'Nicholle', 'Yoon', NULL, 'nicholleyoon@student.olifantys.nl', 2021, 'DKAB', 'F', '1998-01-14');
INSERT INTO public.students VALUES (3690804, 'Berneice', 'Zeno', 'zonder', 'berneicezeno@student.olifantys.nl', 2021, 'DKAB', 'F', '1994-07-18');
INSERT INTO public.students VALUES (3640457, 'Aida', 'Flowers', NULL, 'aidaflowers@student.olifantys.nl', 2021, 'DKAC', 'F', '1997-09-03');


--
-- Data for Name: teachers; Type: TABLE DATA; Schema: public; Owner: exam
--

INSERT INTO public.teachers VALUES (1, 'BIG', 'Billy Bigbottom');
INSERT INTO public.teachers VALUES (3, 'GRE', 'Gerard Greatears');
INSERT INTO public.teachers VALUES (4, 'MAS', 'Michael Massivetusks');
INSERT INTO public.teachers VALUES (5, 'WID', 'Winona Widetrunk');
INSERT INTO public.teachers VALUES (2, 'FUL', 'Fiona Fullofhay');


--
-- Data for Name: uppi; Type: TABLE DATA; Schema: public; Owner: exam
--

INSERT INTO public.uppi VALUES (1, 'duppi');


--
-- Name: courses_course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.courses_course_id_seq', 4, true);


--
-- Name: exam_events_exam_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.exam_events_exam_event_id_seq', 52, true);


--
-- Name: exam_results_exam_result_id_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.exam_results_exam_result_id_seq', 1911, true);


--
-- Name: students_snummer_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.students_snummer_seq', 3690887, true);


--
-- Name: teachers_teacher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.teachers_teacher_id_seq', 6, true);


--
-- Name: uppi_id_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.uppi_id_seq', 1, true);


--
-- Name: courses courses_course_name_key; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_course_name_key UNIQUE (course_name);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (course_id);


--
-- Name: exam_events exam_events_event_name_event_date_key; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_events
    ADD CONSTRAINT exam_events_event_name_event_date_key UNIQUE (event_name, event_date);


--
-- Name: exam_events exam_events_pkey; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_events
    ADD CONSTRAINT exam_events_pkey PRIMARY KEY (exam_event_id);


--
-- Name: exam_results exam_results_pkey; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_results
    ADD CONSTRAINT exam_results_pkey PRIMARY KEY (exam_result_id);


--
-- Name: students students_email_key; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_email_key UNIQUE (email);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (snummer);


--
-- Name: teachers teachers_abreviation_key; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_abreviation_key UNIQUE (abreviation);


--
-- Name: teachers teachers_pkey; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pkey PRIMARY KEY (teacher_id);


--
-- Name: uppi uppi_pkey; Type: CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.uppi
    ADD CONSTRAINT uppi_pkey PRIMARY KEY (id);


--
-- Name: comprehensive_exam_results comprehensive_exam_results_delete; Type: RULE; Schema: public; Owner: exam
--

CREATE RULE comprehensive_exam_results_delete AS
    ON DELETE TO public.comprehensive_exam_results DO INSTEAD  DELETE FROM public.exam_results
  WHERE (exam_results.exam_result_id = old.exam_result_id);


--
-- Name: comprehensive_exam_results comprehensive_exam_results_update; Type: RULE; Schema: public; Owner: exam
--

CREATE RULE comprehensive_exam_results_update AS
    ON UPDATE TO public.comprehensive_exam_results DO INSTEAD  UPDATE public.exam_results SET grade = new.grade;


--
-- Name: exam_results exam_results_fk2; Type: FK CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_results
    ADD CONSTRAINT exam_results_fk2 FOREIGN KEY (exam_event_id) REFERENCES public.exam_events(exam_event_id) ON DELETE CASCADE;


--
-- Name: exam_results exam_results_snummer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: exam
--

ALTER TABLE ONLY public.exam_results
    ADD CONSTRAINT exam_results_snummer_fkey FOREIGN KEY (snummer) REFERENCES public.students(snummer) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

