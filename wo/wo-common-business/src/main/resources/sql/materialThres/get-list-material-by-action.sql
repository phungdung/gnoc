SELECT m.material_id materialId,
  m.material_code materialCode,
  m.material_name materialName ,
  m.unit_id unitId,
  m.unit_name unitName ,
  t.tech_thres techThres ,
  t.warning_thres warningThres ,
  t.free_thres freeThres ,
  t.tech_distanct_thres techDistanctThres ,
  t.warning_distanct_thres warningDistanctThres ,
  t.free_distanct_thres freeDistanctThres ,
  m.material_group_code materialGroupCode ,
  m.material_group_name materialGroupName
FROM wfm.wo_material m,
  wfm.material_thres t
WHERE m.material_id = t.material_id 
