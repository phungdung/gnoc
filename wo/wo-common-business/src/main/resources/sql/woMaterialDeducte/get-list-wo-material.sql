SELECT b.material_code materialCode,
  b.material_name materialName,
  d.item_code actionCode,
  d.item_name actionName,
  e.service_name serviceName,
  e.service_code serviceCode,
  a.material_thres_id materialThresId,
  a.tech_thres techThres,
  a.warning_thres warningThres,
  a.free_thres freeThres,
  a.material_id materialId,
  a.action_id actionId,
  a.service_id serviceId,
  a.infra_type infraType,
  DECODE(a.infra_type, 1,:copperCable, 2,:coaxialCable, 3,'AON',4,'GPON','N/A') technology,
  DECODE(a.tech_distanct_thres,NULL,'','A/'
  || a.tech_distanct_thres) techDistanctThres,
  DECODE(a.warning_distanct_thres,NULL,'','A/'
  || a.warning_distanct_thres) warningDistanctThres,
  DECODE(a.free_distanct_thres,NULL,'','A/'
  || a.free_distanct_thres) freeDistanctThres,
  f.quantity quantity,
  b.unit_name unitName,
  f.user_name userName ,
  b.material_group_name materialGroupName
FROM MATERIAL_THRES a,
  wo_material b,
  COMMON_GNOC.CAT_ITEM d,
  common_gnoc.cat_service e,
  WO_MATERIAL_DEDUCTE f,
  wo g,
  wo_detail h
WHERE d.category_id =
  (SELECT category_id
  FROM common_gnoc.category
  WHERE category_code = 'WO_ACTION_GROUP'
  )
AND a.action_id   = d.item_id(+)
AND a.service_id  = e.service_id(+)
AND a.material_id = b.material_id(+)
AND g.wo_id       = h.wo_id
AND h.wo_id       = f.wo_id
AND a.service_id  = h.service_id(+)
AND a.infra_type  = h.infra_type(+)
AND a.material_id = f.material_id(+)
AND a.action_id   = f.action_id(+)
AND a.is_enable   = 1
AND g.wo_id       = :woId
