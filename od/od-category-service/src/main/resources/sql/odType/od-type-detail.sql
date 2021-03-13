select
  otd.ID id,
  otd.OD_TYPE_ID odTypeId,
  otd.PRIORITY_ID priorityId,
  otd.PROCESS_TIME processTime
from
  WFM.OD_TYPE_DETAIL otd
where
  1=1
