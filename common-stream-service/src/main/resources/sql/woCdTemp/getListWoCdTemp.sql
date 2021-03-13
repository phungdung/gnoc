SELECT a.wo_cd_temp_id woCdTempId,
  a.user_id userId,
  b.username username,
  a.wo_group_id woGroupId,
  c.wo_group_code woGroupCode,
  c.wo_group_name woGroupName,
  a.start_time startTime,
  a.end_time endTime,
  a.is_cd isCd,
  a.status status
FROM WFM.wo_cd_temp a
LEFT JOIN common_gnoc.users b
ON a.user_id=b.user_id
LEFT JOIN WFM.wo_cd_group c
ON a.wo_group_id = c.wo_group_id
WHERE 1          =1
