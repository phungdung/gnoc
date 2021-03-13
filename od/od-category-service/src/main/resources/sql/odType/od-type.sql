select
  ot.OD_TYPE_ID odTypeId,
  ot.OD_TYPE_CODE odTypeCode,
  ot.OD_TYPE_NAME odTypeName,
  ot.STATUS status,
  ot.OD_GROUP_TYPE_ID odGroupTypeId,
  CI.ITEM_NAME odGroupTypeName
from
  WFM.OD_TYPE ot
left join COMMON_GNOC.CAT_ITEM CI on ot.OD_GROUP_TYPE_ID = CI.ITEM_ID
where
  1=1

