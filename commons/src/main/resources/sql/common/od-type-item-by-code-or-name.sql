select
  ot.OD_TYPE_ID itemId,
  ot.OD_TYPE_CODE itemCode,
  ot.OD_TYPE_NAME itemName,
  ot.STATUS status,
  ot.OD_GROUP_TYPE_ID parenItemId

from
  WFM.OD_TYPE ot
where
   ot.status = 1
