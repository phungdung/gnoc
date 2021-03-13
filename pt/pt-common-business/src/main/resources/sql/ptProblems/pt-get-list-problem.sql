SELECT
  a.problem_id problemId,
  a.problem_code problemCode,
  a.problem_name problemName,
  u.unit_name receiveUnitIdStr,
  s.item_name problemState,
  s.item_id stateName,
  pr.item_id stateCode,
  pr.item_name priorityId,
  a.CREATED_TIME createdTime
FROM
  one_tm.problems a,
  common_gnoc.unit u,
  common_gnoc.cat_item s,
  common_gnoc.cat_item pr
WHERE
  a.receive_unit_id = u.unit_id(+)
  AND a.priority_id = pr.item_id
  AND a.problem_state = s.item_id
