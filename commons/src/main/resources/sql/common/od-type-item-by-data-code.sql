SELECT
  OD_TYPE_ID itemId,
  OD_TYPE_NAME itemName,
  OD_TYPE_CODE itemCode,
  STATUS status,
  ot.OD_GROUP_TYPE_ID parenItemId
FROM
  WFM.OD_TYPE
WHERE
  1 = 1 and status = 1
