SELECT
  a.state,
  a.created_time  + :offset * interval '1' hour createdTime
  FROM   one_tm.troubles a, common_gnoc.cat_item p, common_gnoc.cat_item s,
  common_gnoc.cat_item mang, common_gnoc.cat_item alarm
  WHERE   a.priority_id = p.item_id
  AND a.state = s.item_id
  AND a.alarm_group = alarm.item_id
  AND a.type_id = mang.item_id
