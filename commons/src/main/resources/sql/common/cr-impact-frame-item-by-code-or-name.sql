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
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.CR_IMPACT_FRAME') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (
  select
    CIF.IFE_ID,
    CIF.IFE_CODE,
    CIF.START_TIME,
    CIF.END_TIME,
    case
      when llx.LEE_VALUE is null
      then CIF.IFE_NAME
      else llx.LEE_VALUE end IFE_NAME,
    CIF.IS_ACTIVE
  from OPEN_PM.CR_IMPACT_FRAME CIF
  left join list_language_exchange llx on CIF.IFE_ID = llx.BUSSINESS_ID
)
select
  IFE_ID itemId,
  IFE_CODE itemCode,
  IFE_NAME || ' [' || START_TIME || ' - ' || END_TIME || ']' itemName,
  IS_ACTIVE status
from list_result
where NVL(IS_ACTIVE,1)=1
