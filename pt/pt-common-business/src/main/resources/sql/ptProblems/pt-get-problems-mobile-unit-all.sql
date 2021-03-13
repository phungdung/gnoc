select 
--PT cho tiep nhan
0 newPT,
--dang tim nguyen nhan goc
(select count(*) from ONE_TM.problems where state_code in ('PT_QUEUED') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) rcaPT,
--dang tim giai phap triet de
(select count(*) from ONE_TM.problems where state_code in ('PT_DIAGNOSED', 'PT_WA_FOUND','PT_WA_IMPL') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) solutionPT,
--da qua han
(select count(*) from(
select problem_id  from ONE_TM.problems
where es_rca_time is not null and rca_found_time is null 
and to_number(es_rca_time - sysdate) <= 0 and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)
union 
select problem_id  from ONE_TM.problems
where es_wa_time is not null and wa_found_time is null 
and to_number(es_wa_time - sysdate) <= 0 and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)
union 
select problem_id  from ONE_TM.problems
where es_sl_time is not null and sl_found_time is null 
and to_number(es_sl_time - sysdate) <= 0 and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)
)) outOfDatePT
from dual
