select 
  --moi phat sinh
  (select count(*) from ONE_TM.problems where state_code in ('PT_OPEN','PT_UNASSIGNED') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) newPT,
  --da tiep nhan
  (select count(*) from ONE_TM.problems where state_code in ('PT_QUEUED') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) queuePT,
  --da tim duoc nguyen nhan goc
  (select count(*) from ONE_TM.problems where state_code in ('PT_DIAGNOSED') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) diagnosedPT,
  --da tim duoc giai phap tam thoi
  (select count(*) from ONE_TM.problems where state_code in ('PT_WA_FOUND') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) waFoundPT,
  --da tim duoc giai phap triet de
  (select count(*) from ONE_TM.problems where state_code in ('PT_SL_FOUND') and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)) slFoundPT,
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
  )) outOfDatePT,
  --sap qua han
  (select count(*) from(
  select problem_id from ONE_TM.problems
  where es_rca_time is not null and rca_found_time is null
  and to_number(es_rca_time - sysdate) > 0 and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)
  and to_number(es_rca_time - sysdate)/to_number(es_rca_time - created_time)<0.2
  union
  select problem_id from ONE_TM.problems
  where es_wa_time is not null and wa_found_time is null
  and to_number(es_wa_time - sysdate) > 0 and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)
  and to_number(es_wa_time - sysdate)/to_number(es_wa_time - created_time)<0.2
  union
  select problem_id from ONE_TM.problems
  where es_sl_time is not null and sl_found_time is null
  and to_number(es_sl_time - sysdate) > 0 and receive_unit_id IN (SELECT  a.unit_id FROM   common_gnoc.unit a WHERE   LEVEL < 50 START WITH   a.unit_id = :receiveUnitId CONNECT BY   PRIOR a.unit_id = a.parent_unit_id)
  and to_number(es_sl_time - sysdate)/to_number(es_sl_time - created_time)<0.2)
  ) closedOutOfDatePT
from dual
