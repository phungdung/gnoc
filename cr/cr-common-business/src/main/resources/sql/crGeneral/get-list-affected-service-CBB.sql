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
     ass.affected_service_id valueStr,
     ass.parent_id,
     ass.service_code secondValue,
    case
      when llx.LEE_VALUE is null
      then to_char(ass.service_name)
      else  to_char(llx.LEE_VALUE) end displayStr
  from OPEN_PM.affected_services ass
  left join list_language_exchange llx on ass.affected_service_id = llx.BUSSINESS_ID
  where ass.is_active = 1
)
select
   valueStr,
   secondValue,
   displayStr
from list_result
