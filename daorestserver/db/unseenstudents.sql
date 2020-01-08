--
-- PostgreSQL database dump
--
begin work;
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

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: unseenstudents; Type: TABLE; Schema: public; Owner: hom
--

CREATE TABLE public.students (
    snummer integer NOT NULL,
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
-- Data for Name: unseenstudents; Type: TABLE DATA; Schema: public; Owner: hom
--

COPY public.students (snummer, lastname, firstname, tussenvoegsel, email, cohort, student_class, gender, dob) FROM stdin;
3640442	Arlinda	Alldredge	\N	arlindaalldredge@student.olifantys.nl	2021	DKAC	F	2000-03-02
3640443	Sid	Basta	\N	sidbasta@student.olifantys.nl	2021	DKAA	M	1998-11-28
3640444	Genny	Berg	van den	gennyvandenberg@student.olifantys.nl	2021	DKAC	F	1998-06-14
3640445	Ariel	Bodden	\N	arielbodden@student.olifantys.nl	2021	DKAC	F	1999-04-19
3640446	Pierre	Boese	\N	pierreboese@student.olifantys.nl	2019	DKAB	M	2001-10-06
3640447	Elin	Boggs	\N	elinboggs@student.olifantys.nl	2021	DKAC	F	2000-08-09
3640448	Herschel	Buford	\N	herschelbuford@student.olifantys.nl	2021	DKAB	M	2001-03-15
3640449	John	Caro	\N	johncaro@student.olifantys.nl	2021	DKAC	M	1999-06-24
3640450	Shella	Clifton	\N	shellaclifton@student.olifantys.nl	2021	DKAB	F	1996-12-24
3640451	Lenore	Coomes	\N	lenorecoomes@student.olifantys.n	2021	DKAA	F	1999-04-25
3640452	Milagros	Costillo	\N	milagroscostillo@student.olifantys.nl	2021	DKAC	M	1999-04-17
3640453	Horace	Crofoot	\N	horacecrofoot@student.olifantys.nl	2021	DKAA	M	1997-11-05
3640454	Nathan	Dudding	\N	nathandudding@student.olifantys.nl	2021	DKAC	M	1999-03-08
3640455	Ezequiel	Fairweather	\N	ezequielfairweather@student.olifantys.nl	2021	DKAB	M	2001-06-26
3640456	Judson	Fishel	\N	judsonfishel@student.olifantys.nl	2021	DKAA	M	2001-07-12
3640457	Aida	Flowers	\N	aidaflowers@student.olifantys.nl	2021	DKAC	F	1998-11-06
3640458	Jennie	Foerster	\N	jenniefoerster@student.olifantys.nl	2021	DKAC	F	2000-08-06
3640459	Dexter	Follmer	\N	dexterfollmer@student.olifantys.nl	2021	DKAC	M	2000-09-11
3640460	Clair	Fuselier	\N	clairfuselier@student.olifantys.nl	2021	DKAC	F	1996-12-17
3640461	Ulrike	Garretson	\N	ulrikegarretson@student.olifantys.nl	2021	DKAB	F	2001-07-22
3640462	Billy	Gerst	\N	billygerst@student.olifantys.nl	2021	DKAB	M	1998-07-08
3640463	Marg	Gillman	\N	marggillman@student.olifantys.nl	2021	DKAA	F	1999-06-09
3640464	Onita	Gloss	\N	onitagloss@student.olifantys.nl	2021	DKAB	F	2001-04-13
3640465	Josie	Gramling	\N	josiegramling@student.olifantys.nl	2021	DKAA	F	2001-03-24
3640466	Mark	Hodak	van der	markhodak@student.olifantys.nl	2021	DKAA	F	1996-12-13
3640467	Barney	Holz	to	barneyholz@student.olifantys.nl	2021	DKAC	M	1998-07-17
3640468	Glinda	Joines	\N	glindajoines@student.olifantys.nl	2021	DKAB	F	2000-02-06
3640469	Talitha	Link	\N	talithalink@student.olifantys.nl	2021	DKAA	F	2000-04-06
3640470	Inez	Lukach	\N	inezlukach@student.olifantys.nl	2021	DKAA	F	2001-06-03
3640471	Oma	Mcinnis	van	omamcinnis@student.olifantys.nl	2021	DKAB	M	1997-10-10
3640472	Edie	Mckinnon	op de	ediemckinnon@student.olifantys.nl	2021	DKAB	M	2001-05-12
3640473	Codi	Mcspadden	\N	codimcspadden@student.olifantys.nl	2021	DKAC	M	1999-08-11
3640474	Hortencia	Melendrez	\N	hortenciamelendrez@student.olifantys.nl	2021	DKAB	F	1997-05-21
3640475	Luba	Mendoza	\N	lubamendoza@student.olifantys.nl	2021	DKAC	F	2000-05-20
3640476	Cathie	Minor	\N	cathieminor@student.olifantys.nl	2021	DKAA	F	2000-08-27
3640477	Elijah	Moench	\N	elijahmoench@student.olifantys.nl	2021	DKAC	M	1997-10-24
3640478	Lauran	Moncayo	\N	lauranmoncayo@student.olifantys.nl	2021	DKAA	M	2001-08-11
3640479	Brigida	Mowbray	\N	brigidamowbray@student.olifantys.nl	2021	DKAA	F	1998-09-13
3640480	Chanell	Musselwhite	\N	chanellmusselwhite@student.olifantys.nl	2021	DKAB	F	1998-08-08
3640481	Myriam	Nanney	\N	myriamnanney@student.olifantys.nl	2021	DKAA	F	2001-03-17
3640482	Socorro	Negrin	\N	socorronegrin@student.olifantys.nl	2021	DKAC	M	2000-06-16
3640483	Jolie	Neu	\N	jolieneu@student.olifantys.nl	2019	DKAC	F	1998-08-10
3640484	Theola	Occhipinti	\N	theolaocchipinti@student.olifantys.nl	2021	DKAB	F	1999-08-27
3640485	Claire	Otero	\N	claireotero@student.olifantys.nl	2021	DKAB	F	1998-02-11
3640486	Jeremy	Philyaw	\N	jeremyphilyaw@student.olifantys.nl	2021	DKAC	M	1997-01-18
3640487	Monnie	Pipkin	\N	monniepipkin@student.olifantys.nl	2021	DKAC	F	1998-02-21
3640488	Daniele	Podesta	\N	danielepodesta@student.olifantys.nl	2021	DKAB	F	2000-02-24
3640489	Tomika	Printz	\N	tomikaprintz@student.olifantys.nl	2021	DKAC	F	2000-04-13
3640490	Leilani	Quirion	\N	leilaniquirion@student.olifantys.nl	2023	DKDD	F	1997-06-26
3640491	Felton	Rael	\N	feltonrael@student.olifantys.nl	2022	DKAB	M	1999-06-10
3640492	Sylvia	Rahaim	\N	sylviarahaim@student.olifantys.nl	2021	DKAB	F	2000-03-31
3640493	Georgiana	Reinhart	\N	georgianareinhart@student.olifantys.nl	2021	DKAC	F	1997-11-17
3640494	Jonell	Runion	\N	jonellrunion@student.olifantys.nl	2021	DKAA	M	1998-09-25
3640495	Annette	Schaaf	\N	annetteschaaf@student.olifantys.nl	2021	DKAB	F	1998-02-21
3640496	Shanell	Spina	\N	shanellspina@student.olifantys.nl	2019	DKAB	F	1997-02-23
3640497	Mahalia	Stee	\N	mahaliastee@student.olifantys.nl	2021	DKAC	F	1998-01-03
3640498	Zora	Testa	\N	zoratesta@student.olifantys.nl	2021	DKAB	F	2001-10-19
3640499	Georgeanna	Tolley	\N	georgeannatolley@student.olifantys.nl	2021	DKAA	F	1998-08-22
3690793	Anitra	Troop	\N	anitratroop@student.olifantys.nl	2020	TKDD	F	2000-05-07
3690794	Millicent	Uzzle	\N	millicentuzzle@student.olifantys.nl	2021	DKAC	F	1996-12-20
3690795	Royce	Wafford	\N	roycewafford@student.olifantys.nl	2021	DKAA	M	1998-04-11
3690796	Harriette	Weiner	\N	harrietteweiner@student.olifantys.nl	2021	DKAC	F	1997-08-28
3690797	Shanta	Wiemann	\N	shantawiemann@student.olifantys.nl	2021	DKAA	F	1999-06-16
3690798	Fawn	Wier	\N	fawnwier@student.olifantys.nl	2021	DKAC	F	1999-03-09
3690799	Yoshiko	Wordlaw	\N	yoshikowordlaw@student.olifantys.nl	2021	DKAC	F	1998-07-21
3690800	Janett	Xie	\N	janettxie@student.olifantys.nl	2021	DKAC	F	1997-04-10
3690801	Mikki	Müller-Yang	\N	mikkiyang@student.olifantys.nl	2021	DKAA	M	1997-06-16
3690802	Shelia	Yazzieß	\N	sheliayazzies@student.olifantys.nl	2021	DKAA	F	1999-05-22
3690803	Nicholle	Yoon	\N	nicholleyoon@student.olifantys.nl	2021	DKAB	F	1996-12-15
3690804	Berneice	Zeno	zonder	berneicezeno@student.olifantys.nl	2021	DKAB	F	2000-07-31
\.


--
-- Name: unseenstudents unseen_email_un; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT unseen_email_un UNIQUE (email);


--
-- Name: unseenstudents unseenstudents_pk; Type: CONSTRAINT; Schema: public; Owner: hom
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pk PRIMARY KEY (snummer);


--
-- PostgreSQL database dump complete
--
commit;
