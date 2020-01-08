-- one sql parameter, the json document
with js as (
     select cast(? as jsonb)  j ),
-- get the teachers array out as virtual table, then aggregate into a sql array
 teach as (select array_agg(t) tt from (select jsonb_array_elements_text(j->'examiners' ) t from js) t),
-- get and insert the exam meta data like event_date, event_name  and the teachers from the json and the previous teach 'table'
 meta as (insert into exam_events (event_name,event_date,examiners) select j->>'event_name' as event_name,
      cast(j->>'event_date' as date) as event_date, tt as examiners from js cross join teach returning *),
-- get the data from the student number,grade  json-array as a two column table. sresult is a table definition  with exactly those columns
 res1 as ( select  snummer,grade from js,jsonb_populate_recordset(null::sresult, js.j->'results') ),
-- now do the insert results combine with a reference to the exam_events table (three columns). 
 res2 as ( insert into exam_results (exam_event_id,snummer,grade)
            select exam_event_id,     res1.snummer, res1.grade from res1,meta 
            returning * ),
-- produce a result but pack it as a json array, because that is what the client likes
 res3 as ( select snummer,lastname,firstname,tussenvoegsel,email,student_class,
       event_name, event_date,examiners, grade, exam_result_id
       from students join res2 using(snummer) join meta using(exam_event_id))
-- wrap as json array.
select array_to_json(array_agg(o),true) from res3 o;