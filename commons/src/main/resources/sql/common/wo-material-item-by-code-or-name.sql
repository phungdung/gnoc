select
  wm.MATERIAL_ID itemId,
  wm.MATERIAL_CODE itemCode,
  wm.MATERIAL_NAME itemName,
  wm.UNIT_NAME description,
  wm.is_enable status,
--   wm.MATERIAL_GROUP_ID parenItemId,
  wm.MATERIAL_GROUP_NAME parenItemName
from
  WFM.WO_MATERIAL wm
where
   wm.is_enable = 1
