SELECT
  UNIT_ID itemId,
  UNIT_NAME itemName,
  UNIT_CODE itemCode,
  DESCRIPTION description,
  STATUS status
FROM COMMON_GNOC.UNIT
WHERE 1 = 1
and status = 1
