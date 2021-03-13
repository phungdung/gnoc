select
  wt.WO_TYPE_ID itemId,
  wt.WO_TYPE_CODE itemCode,
  wt.WO_TYPE_NAME itemName,
  wt.IS_ENABLE status,
  wt.WO_GROUP_TYPE parenItemId
from
  WFM.WO_TYPE wt
where
   wt.IS_ENABLE = 1
