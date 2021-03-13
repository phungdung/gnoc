SELECT  a.wo_material_deducte_id woMaterialDeducteId,
  a.action_id actionId,
  a.create_date createDate,
  a.is_deducte isDeducte,
  a.material_id materialId,
  a.user_name userName,
  a.wo_id woId,
  a.quantity,
  a.reason_id reasonId,
  a.send_im_result sendImResult,
  a.send_im_time sendImTime,
  a.user_id userId,
  c.item_name actionName ,
  m.material_name materialName,
  m.UNIT_NAME unitName ,
  m.material_group_name materialGroupName ,
  m.material_group_code materialGroupCode
FROM wo_material_deducte a
 inner join  common_gnoc.cat_item c on a.action_id = c.item_id
 inner join wo_material m on a.material_id = m.material_id
WHERE a.wo_id = :woId
