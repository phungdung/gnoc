SELECT *
FROM
  (SELECT n.mr_node_id mrNodeId ,
    NVL(cl.device_type, cl.DEVICE_TYPE_ALL) deviceTypeAll ,
    cl.CHECKLIST_ID checklistId ,
    cl.CONTENT content ,
    b2.STATUS status ,
    b2.COMMENTS comments ,
    b2.NODE_CHECKLIST_ID nodeChecklistId ,
    n.wo_id,
    n.MR_NODE_ID ,
    cl.TARGET
  FROM open_pm.MR_CHECKLIST cl
  INNER JOIN open_pm.mr_schedule_tel s
  ON cl.market_code    = s.market_code
  AND (cl.device_type IS NULL
  OR cl.device_type    = s.device_type)
  AND cl.array_code    = s.array_code
  AND cl.cycle         = s.MR_HARD_CYCLE
  INNER JOIN MR_NODES n
  ON n.mr_id      = s.MR_ID
  AND n.node_code = s.device_code
  LEFT JOIN MR_NODE_CHECKLIST b2
  ON b2.MR_NODE_ID    = n.MR_NODE_ID
  AND b2.CHECKLIST_ID = cl.CHECKLIST_ID
  UNION
  SELECT n.mr_node_id mrNodeId ,
    NVL(cl.device_type, cl.DEVICE_TYPE_ALL) deviceTypeAll ,
    cl.CHECKLIST_ID checklistId ,
    cl.CONTENT content ,
    b2.STATUS status ,
    b2.COMMENTS comments ,
    b2.NODE_CHECKLIST_ID nodeChecklistId ,
    n.wo_id,
    n.MR_NODE_ID ,
    cl.TARGET
  FROM open_pm.MR_CHECKLIST cl
  INNER JOIN open_pm.mr_schedule_cd s
  ON cl.market_code    = s.market_code
  AND (cl.device_type IS NULL
  OR cl.device_type    = s.device_type)
  AND cl.array_code    = 'Hệ thống nguồn và hệ thống phụ trợ tại Tổng trạm KV'
  AND cl.cycle         = s.CYCLE
  INNER JOIN MR_NODES n
  ON n.mr_id      = s.MR_ID
  AND n.node_code = s.device_name
  LEFT JOIN MR_NODE_CHECKLIST b2
  ON b2.MR_NODE_ID    = n.MR_NODE_ID
  AND b2.CHECKLIST_ID = cl.CHECKLIST_ID
  )
WHERE 1        = 1
AND wo_id      = :wo_id
AND MR_NODE_ID = :mrNodeId
