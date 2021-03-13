select 
--PT cho tiep nhan
(select count(*) from ONE_TM.problems where state_code in ('PT_OPEN','PT_UNASSIGNED') and receive_unit_id IN (:receiveUnitId)) newPT,
--dang tim nguyen nhan goc
(select count(*) from ONE_TM.problems where state_code in ('PT_QUEUED') and receive_unit_id IN (:receiveUnitId)) rcaPT,
--dang tim giai phap triet de
(select count(*) from ONE_TM.problems where state_code in ('PT_DIAGNOSED', 'PT_WA_FOUND','PT_WA_IMPL') and receive_unit_id IN (:receiveUnitId)) solutionPT,
--da qua han
(select count(*) from(
select problem_id  from ONE_TM.problems
where es_rca_time is not null and rca_found_time is null
and to_number(es_rca_time - sysdate) <= 0 and receive_unit_id IN (:receiveUnitId)
union
select problem_id  from ONE_TM.problems
where es_wa_time is not null and wa_found_time is null
and to_number(es_wa_time - sysdate) <= 0 and receive_unit_id IN (:receiveUnitId)
union
select problem_id  from ONE_TM.problems
where es_sl_time is not null and sl_found_time is null
and to_number(es_sl_time - sysdate) <= 0 and receive_unit_id IN (:receiveUnitId)
)) outOfDatePT
from dual
