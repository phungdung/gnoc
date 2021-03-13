WITH 
list_language_exchange AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = :p_system) CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE 
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = :p_bussiness) CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
     list_data AS (
  select
         cr_process_id ,
         cr_process_name ,
         cr_process_code ,
         parent_id,
         RPAD('--|', (level-1)*3, '--|')
           || cr_process_name AS tree,
         level clevel,
         is_active,
         CONNECT_BY_ROOT cr_process_id                       AS root_id,
         LTRIM(SYS_CONNECT_BY_PATH(cr_process_id, '-'), '-') AS path,
         CONNECT_BY_ISLEAF                                   AS isLEAF,
         cps.impact_type ,
         cps.RISK_LEVEL AS thirdValue
  from open_pm.cr_process cps
  where level < 3
  START WITH (
                 cps.parent_id IS NULL
                   AND is_active    =1
                 )
  CONNECT BY parent_id = PRIOR cr_process_id
  ORDER SIBLINGS BY cr_process_id
    )
select
       ld.cr_process_id AS valueStr,
       case
         when llx.LEE_VALUE is null
                 then ld.tree
         else RPAD('--|', (clevel-1)*3, '--|')
                || llx.LEE_VALUE end displayStr,
       ld.impact_type  || ',' || ld.isleaf AS secondValue ,
       thirdValue,
       path,
       clevel
from list_data ld
left join list_language_exchange llx on ld.cr_process_id = llx.BUSSINESS_ID
WHERE ld.cr_process_id IN
      (SELECT cps.cr_process_id
       FROM open_pm.cr_process cps
       START WITH ( 1, cps.cr_process_id ) IN (:idx)
       CONNECT BY PRIOR parent_id    = cr_process_id
      )
order by ld.path
