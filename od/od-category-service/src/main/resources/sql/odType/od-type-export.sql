select
  ot.OD_TYPE_ID odTypeId,
  ot.OD_TYPE_CODE odTypeCode,
  ot.OD_TYPE_NAME odTypeName,
  ot.STATUS status,
  ot.OD_GROUP_TYPE_ID odGroupTypeId,
  cit.ITEM_NAME odGroupTypeName,
  cits.ITEM_NAME priorityName,
  otd.ID idOdDetail,
  otd.PRIORITY_ID priorityId,
  otd.PROCESS_TIME processTime
from
  WFM.OD_TYPE ot,
  common_gnoc.CAT_ITEM cit,
  common_gnoc.CAT_ITEM cits,
  WFM.OD_TYPE_DETAIL otd
where
  1=1
  and ot.OD_TYPE_ID = otd.OD_TYPE_ID (+)
  and ot.OD_GROUP_TYPE_ID = cit.ITEM_ID (+)
  and otd.PRIORITY_ID = cits.ITEM_ID (+)
