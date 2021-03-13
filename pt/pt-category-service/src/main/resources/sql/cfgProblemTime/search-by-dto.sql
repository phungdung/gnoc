SELECT cfg.ID id, cfg.cfg_code cfgCode, cfg.type_code typeCode,
    tName.item_name typeName,
    cfg.priority_code priorityCode,
    pName.item_name priorityName,
    cfg.rca_found_time rcaFoundTime,
    cfg.wa_found_time waFoundTime,
    cfg.sl_found_time slFoundTime,
    cfg.last_update_time lastUpdateTime
  FROM
  one_tm.cfg_problem_time_process cfg, common_gnoc.cat_item tName, common_gnoc.cat_item pName
  WHERE
  cfg.type_code = tName.item_code
  AND cfg.priority_code = pName.item_code
