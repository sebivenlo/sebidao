--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3 (Ubuntu 10.3-1.pgdg16.04+1)
-- Dumped by pg_dump version 10.3 (Ubuntu 10.3-1.pgdg16.04+1)

BEGIN WORK;
--
-- Name: comprehensive_exam_results; Type: VIEW; Schema: public; Owner: hom
--
DROP VIEW IF EXISTS public.comprehensive_exam_results;
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

CREATE OR REPLACE RULE comprehensive_exam_results_update AS ON UPDATE
TO  comprehensive_exam_results
 DO INSTEAD (UPDATE exam_results SET grade=NEW.grade);

CREATE OR REPLACE RULE comprehensive_exam_results_delete AS ON DELETE
 TO  comprehensive_exam_results
 DO INSTEAD (DELETE FROM exam_results WHERE exam_result_id=OLD.exam_result_id);


ALTER TABLE public.comprehensive_exam_results OWNER TO exam;

--
-- PostgreSQL database dump complete
--

COMMIT;
