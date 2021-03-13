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
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = 'OPEN_PM') CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.IMPACT_SEGMENT') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (
  select
    ISM.IMPACT_SEGMENT_ID,
    ISM.IMPACT_SEGMENT_CODE,
    case
      when llx.LEE_VALUE is null
      then ISM.IMPACT_SEGMENT_NAME
      else llx.LEE_VALUE end IMPACT_SEGMENT_NAME,
    ISM.APPLIED_SYSTEM,
    ISM.IS_ACTIVE
  from OPEN_PM.IMPACT_SEGMENT ISM
  left join list_language_exchange llx on ISM.IMPACT_SEGMENT_ID = llx.BUSSINESS_ID
)
select
  IMPACT_SEGMENT_ID itemId,
  IMPACT_SEGMENT_CODE itemCode,
  IMPACT_SEGMENT_NAME itemName,
  IS_ACTIVE status
from list_result
where IS_ACTIVE=1
