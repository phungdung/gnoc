SELECT id id,
  sr_id srId,
  dt_id dtId,
  dt_name dtName,
  template_id templateId,
  type type,
  IP_NODE ipNode
FROM sr_mop
WHERE sr_id NOT IN
  (SELECT sr_id FROM sr
  )
