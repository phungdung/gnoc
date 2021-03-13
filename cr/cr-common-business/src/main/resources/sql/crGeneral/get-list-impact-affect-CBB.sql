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
  where LE.APPLIED_SYSTEM = :applied_system and LE.APPLIED_BUSSINESS = :bussiness and LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (
  select
     al.affected_level_id valueStr,
    case
      when llx.LEE_VALUE is null
      then al.affected_level_name
      else llx.LEE_VALUE end displayStr
  from OPEN_PM.affected_level al
  left join list_language_exchange llx on al.affected_level_id = llx.BUSSINESS_ID
  where al.is_active = 1 and al.applied_system = 2
)
select
 *
from list_result
order by NLSSORT(displayStr,'NLS_SORT=vietnamese')
