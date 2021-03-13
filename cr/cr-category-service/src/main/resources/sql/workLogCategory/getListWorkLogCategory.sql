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
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.WORK_LOG_CATEGORY') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
listNeed AS (
SELECT
t1.WLAY_ID wlayId,
t1.WLAY_TYPE wlayType,
t1.WLAY_CODE wlayCode,
t1.WLAY_NAME wlayName,
t1.WLAY_DESCRIPTION wlayDescription,
t1.WLAY_IS_ACTIVE wlayIsActive,
t1.WLAY_IS_EDITABLE wlayIsEditable
FROM OPEN_PM.WORK_LOG_CATEGORY t1
WHERE 1 = 1
),
list_result AS (
SELECT
listNeed.wlayId wlayId,
listNeed.wlayType wlayType,
listNeed.wlayCode wlayCode,
case
when llx.LEE_VALUE is null
then listNeed.wlayName
else llx.LEE_VALUE
end wlayName,
listNeed.wlayDescription wlayDescription,
listNeed.wlayIsActive wlayIsActive,
listNeed.wlayIsEditable wlayIsEditable
FROM listNeed left join list_language_exchange llx on listNeed.wlayId = llx.BUSSINESS_ID
)
SELECT
list_result.wlayId,
list_result.wlayType,
list_result.wlayCode,
list_result.wlayName,
t1.UGCY_NAME wlayNameType,
list_result.wlayDescription,
list_result.wlayIsActive,
list_result.wlayIsEditable
FROM list_result LEFT JOIN OPEN_PM.USER_GROUP_CATEGORY t1 ON t1.UGCY_ID = list_result.wlayType
WHERE 1 = 1

