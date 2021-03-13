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
  where LE.APPLIED_SYSTEM = 2--:applied_system
  and LE.APPLIED_BUSSINESS = 10--:bussiness
  and LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (
  select
    ist.impact_segment_id impactSegmentId,
    ist.impact_segment_code impactSegmentCode,
    case
      when llx.LEE_VALUE is null
      then ist.impact_segment_name
      else llx.LEE_VALUE end impactSegmentName
  from OPEN_PM.impact_segment ist
  left
  join list_language_exchange llx on ist.impact_segment_id = llx.BUSSINESS_ID
  where ist.is_active = 1 and ist.applied_system = 2
)
select
 *
from list_result
order by impactSegmentId
