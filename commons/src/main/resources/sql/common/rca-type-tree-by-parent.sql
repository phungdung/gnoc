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
  where LE.APPLIED_SYSTEM = 1 and LE.APPLIED_BUSSINESS = 3 and LE.LEE_LOCALE = :p_leeLocale
),
list_data AS (
  select
    ITEM_ID key,
    ITEM_ID value,
    ITEM_NAME title,
    CONNECT_BY_ISLEAF isleaf,
    0 disabled
  from COMMON_GNOC.CAT_ITEM
  where STATUS = 1
    and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE='PT_RCA_TYPE' and STATUS = 1)
    and ((:p_parent is not null and LEVEL = 2) or (:p_parent is null and PARENT_ITEM_ID is null))
  start with (:p_parent is null and PARENT_ITEM_ID is null) or (:p_parent is not null and ITEM_ID = :p_parent)
  connect by prior ITEM_ID = PARENT_ITEM_ID
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
