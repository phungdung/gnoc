SELECT
Q.CFG_ID cfgId,
Q.CFG_NAME cfgName,
Q.UNIT_ID unitId,
(E.UNIT_NAME ||'('|| E.UNIT_CODE ||')')  unitName,
Q.LOCATION_ID locationId,
Q.LOCATION_NAME locationName,
Q.PRIORITY_ID priorityId,
Q.PRIORITY_NAME priorityName,
Q.LEVEL_ID levelId,
w.ITEM_NAME levelName,
Q.TIME_PROCESS timeProcess
FROM CFG_SMS_GOING_OVERDUE Q, COMMON_GNOC.UNIT E,
(select * from CAT_ITEM  where CATEGORY_ID in (select  CATEGORY_ID from CATEGORY where CATEGORY_CODE = 'SMS_GOING_OVERDUE_LEVEL')) w
