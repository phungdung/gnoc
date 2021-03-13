WITH
list_language_exchange AS (
SELECT
  cat.ITEM_ID,
  cat.ITEM_NAME,
  LE.BUSSINESS_ID,
  LE.LEE_VALUE
FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
  Select CATEGORY_ID from COMMON_GNOC.CATEGORY
  where CATEGORY_CODE= 'GNOC_SHIFT' and EDITABLE = 1)
  ) cat
LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
ON LE.BUSSINESS_ID = cat.ITEM_ID
  AND LE.LEE_LOCALE = :p_leeLocale
  and LE.APPLIED_SYSTEM = 1
  and LE.APPLIED_BUSSINESS = 3
)
SELECT t1.ID id,
t1.USER_NAME userName,
t1.USER_ID userId,
t1.UNIT_NAME unitName,
t1.UNIT_ID unitId,
t1.CREATED_TIME createdTime,
t1.LAST_UPDATE_TIME lastUpdateTime,
t1.STATUS status,
t1.SHIFT_ID shiftId,
CASE t1.STATUS
WHEN 1 THEN :receivedShift
ELSE :waitingShift END statusName,
case
when lle.LEE_VALUE is null
then to_char(lle.ITEM_NAME)
else to_char(lle.LEE_VALUE) end shiftName
FROM COMMON_GNOC.SHIFT_HANDOVER t1
LEFT JOIN list_language_exchange lle ON t1.SHIFT_ID = lle.ITEM_ID
WHERE t1.UNIT_ID = :unitId
