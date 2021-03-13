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
    cr_process_id itemId,
    cr_process_name itemName
  from open_pm.cr_process
)
select
  ld.itemId,
  ld.itemName,
  case
    when llx.LEE_VALUE is null
    then ld.itemName
    else llx.LEE_VALUE end itemName
from list_data ld
left join list_language_exchange llx on ld.itemId = llx.BUSSINESS_ID
where 1 = 1
