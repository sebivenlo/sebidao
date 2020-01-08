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
-- Data for Name: exam_events; Type: TABLE DATA; Schema: public; Owner: hom
--

COPY public.exam_events (exam_event_id, event_name, event_date, examiners) FROM stdin;
1	PRC2	2018-05-03	{ham,him}
\.


--
-- Name: exam_events_exam_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: hom
--

SELECT pg_catalog.setval('public.exam_events_exam_event_id_seq', 1, true);


--
-- PostgreSQL database dump complete
--

