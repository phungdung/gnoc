SELECT item_id cfgId,
  item_name cfgName,
  item_code cfgCode,
  (SELECT a.wo_type_id
  FROM wo_type_cfg_required a
  WHERE a.cfg_id    = c.item_id
  AND (a.wo_type_id = :woTypeId
  OR a.wo_type_id  IS NULL)
  ) woTypeId ,
  (SELECT a.value
  FROM wo_type_cfg_required a
  WHERE a.cfg_id    = c.item_id
  AND (a.wo_type_id = :woTypeId
  OR a.wo_type_id  IS NULL)
  ) value ,
  (SELECT a.id
  FROM wo_type_cfg_required a
  WHERE a.cfg_id    = c.item_id
  AND (a.wo_type_id = :woTypeId
  OR a.wo_type_id  IS NULL)
  ) id
FROM common_gnoc.cat_item c
WHERE c.category_id =
  (SELECT d.category_id
  FROM common_gnoc.category d
  WHERE d.category_code = 'CFG_REQUIRED_WO_TYPE'
  )
