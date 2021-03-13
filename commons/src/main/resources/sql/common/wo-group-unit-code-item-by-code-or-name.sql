select
  GROUP_UNIT_ID itemId,
  GROUP_UNIT_CODE itemCode,
  GROUP_UNIT_CODE itemName,
  IS_ACTIVE status
from
  OPEN_PM.GROUP_UNIT
where
   IS_ACTIVE = 1
