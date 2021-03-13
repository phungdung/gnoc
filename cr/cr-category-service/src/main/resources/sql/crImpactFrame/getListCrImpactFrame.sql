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
listNeed AS (
SELECT
t1.IFE_ID impactFrameId,
t1.IFE_CODE impactFrameCode,
t1.IFE_NAME impactFrameName,
t1.START_TIME startTime,
t1.END_TIME endTime,
t1.DESCRIPTION description,
t1.IS_ACTIVE isActive,
t1.IS_EDITABLE isEditable,
t1.CREATED_TIME createdTime,
t1.CREATED_USER createdUser,
t1.UPDATED_TIME updatedTime,
t1.UPDATED_USER updatedUser
FROM OPEN_PM.CR_IMPACT_FRAME t1
WHERE 1 = 1
),
list_result AS (
SELECT
listNeed.impactFrameId impactFrameId,
listNeed.impactFrameCode impactFrameCode,
case
when llx.LEE_VALUE is null
then listNeed.impactFrameName
else llx.LEE_VALUE
end impactFrameName,
listNeed.startTime startTime,
listNeed.endTime endTime,
listNeed.description description,
listNeed.isActive isActive,
listNeed.isEditable isEditable,
listNeed.createdTime createdTime,
listNeed.createdUser createdUser,
listNeed.updatedTime updatedTime,
listNeed.updatedUser updatedUser
FROM listNeed left join list_language_exchange llx on listNeed.impactFrameId = llx.BUSSINESS_ID
)
SELECT
list_result.impactFrameId,
list_result.impactFrameCode,
list_result.impactFrameName,
list_result.startTime,
list_result.endTime,
list_result.description,
list_result.isActive isActive,
list_result.isEditable isEditable,
list_result.createdTime createdTime,
list_result.createdUser createdUser,
list_result.updatedTime updatedTime,
list_result.updatedUser updatedUser
FROM list_result
WHERE 1 = 1
