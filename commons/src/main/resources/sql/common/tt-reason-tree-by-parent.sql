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
  where LE.APPLIED_SYSTEM = 3 and LE.APPLIED_BUSSINESS = 1 and LE.LEE_LOCALE = :leeLocale
),
list_data AS (
 select
  a.id key,
  a.id value,
  a.REASON_NAME title,
  CONNECT_BY_ISLEAF isleaf,
  0 disabled
FROM ONE_TM.CAT_REASON a
 WHERE 1 = 1
 AND ((:p_parent is not null and LEVEL = 2) or (:p_parent is null and a.PARENT_ID is null))
 AND (:typeId is null or to_char(a.type_id) = :typeId)
start with (:p_parent is null and a.PARENT_ID is null) or (:p_parent is not null and  to_char(a.ID) = :p_parent)
connect by prior a.ID = a.PARENT_ID
)
select
  ld.key,
  ld.value,
  case
    when llx.LEE_VALUE is null
    then ld.title
    else TO_CHAR(llx.LEE_VALUE) end title,
  ld.isleaf,
  ld.disabled
from list_data ld
left join list_language_exchange llx on ld.value = llx.BUSSINESS_ID
order by ld.title ASC
