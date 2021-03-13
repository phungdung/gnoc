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
    cr_process_id key,
    cr_process_id value,
    cr_process_name title,
    CONNECT_BY_ISLEAF isleaf,
    0 disabled
  from open_pm.cr_process
  where ((:p_parent is not null and LEVEL = 2) or (:p_parent is null and parent_id is null))
  start with (:p_parent is null and parent_id is null) or (:p_parent is not null and cr_process_id = :p_parent)
  connect by prior cr_process_id = parent_id
)
select
  ld.key,
  ld.value,
  case
    when llx.LEE_VALUE is null
    then ld.title
    else llx.LEE_VALUE end title,
  ld.isleaf,
  ld.disabled
from list_data ld
left join list_language_exchange llx on ld.value = llx.BUSSINESS_ID
order by ld.title ASC
