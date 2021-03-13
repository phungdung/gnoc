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
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.DEVICE_TYPES') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (
  select
    DT.DEVICE_TYPE_ID,
    DT.DEVICE_TYPE_CODE,
    case
      when llx.LEE_VALUE is null
      then DT.DEVICE_TYPE_NAME
      else llx.LEE_VALUE end DEVICE_TYPE_NAME,
    DT.IS_ACTIVE
  from OPEN_PM.DEVICE_TYPES DT
  left join list_language_exchange llx on DT.DEVICE_TYPE_ID = llx.BUSSINESS_ID
)
select
  DEVICE_TYPE_ID itemId,
  DEVICE_TYPE_CODE itemCode,
  DEVICE_TYPE_NAME itemName,
  IS_ACTIVE status
from list_result
where IS_ACTIVE=1
