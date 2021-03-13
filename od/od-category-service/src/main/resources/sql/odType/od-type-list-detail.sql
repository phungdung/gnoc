select
  otd.ID id,
  otd.PROCESS_TIME processTime,
  otd.OD_TYPE_ID odTypeId,
  cit.ITEM_NAME priorityName,
  cit.ITEM_ID priorityId,
  cit.ITEM_CODE priorityCode
from
  COMMON_GNOC.CAT_ITEM cit
LEFT JOIN
  WFM.OD_TYPE_DETAIL otd
ON
  cit.ITEM_ID = otd.PRIORITY_ID and
  otd.OD_TYPE_ID =:odTypeId
where
  cit.CATEGORY_ID = (
  Select CATEGORY_ID
  from COMMON_GNOC.CATEGORY
  where CATEGORY_CODE='OD_PRIORITY' and STATUS = 1)
