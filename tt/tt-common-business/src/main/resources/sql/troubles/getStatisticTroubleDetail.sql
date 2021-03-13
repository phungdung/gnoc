SELECT a.trouble_id troubleId,
  p.item_code priorityCode,
  s.item_code statusCode,
  s.item_name status,
  p.item_name priority,
  a.receive_unit_name receiveUnit,
  TO_CHAR(a.created_time,'dd/MM/yyyy HH24:mi:ss') createdTime,
  a.trouble_code troubleCode,
  a.trouble_name troubleName,
  a.create_unit_name createUnit,
  CASE
    WHEN s.item_code         IN('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED')
    THEN NVL(a.time_process,0)- (NVL(a.time_used,0) + ROUND( (NVL(a.clear_time,sysdate)-NVL(a.assign_time_temp,a.assign_time))*24,2))
    WHEN s.item_code         IN ('DEFERRED')
    THEN NVL(a.time_process,0)- NVL(a.time_used,0)
    ELSE NULL
  END remainTime,
  TO_CHAR(a.last_update_time,'dd/MM/yyyy HH24:mi:ss') lastUpdateTime
FROM troubles a,
  common_gnoc.cat_item s,
  common_gnoc.cat_item p
WHERE a.state     = s.item_id
AND a.priority_id = p.item_id
