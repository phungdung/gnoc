select
  wcg.WO_GROUP_ID itemId,
  wcg.WO_GROUP_CODE itemCode,
  wcg.WO_GROUP_NAME itemName,
  wcg.IS_ENABLE status,
  wcg.GROUP_TYPE_ID parenItemId
from
  WFM.WO_CD_GROUP wcg
where
   wcg.IS_ENABLE = 1
