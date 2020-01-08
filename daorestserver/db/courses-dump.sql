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
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: exam
--

COPY public.courses (course_id, course_credits, course_name, course_description, course_teachers) FROM stdin;
1	5	PRC2	Programming Concepts 2	{HOM,HVD}
2	5	PRJ2	Logistics Rest Application	{MON,HOM,HVD,SOB,DOS}
\.


--
-- Name: courses_course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: exam
--

SELECT pg_catalog.setval('public.courses_course_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--

