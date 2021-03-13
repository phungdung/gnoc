SELECT t1.sr_id srId,
  t1.sr_code srCode,
  t1.title title,
  t1.status status,
  TO_CHAR( t1.start_time, 'dd/MM/yyyy HH24:mi' ) startTime,
  TO_CHAR( t1.end_time, 'dd/MM/yyyy HH24:mi' ) endTime,
  t1.created_user createdUser,
  TO_CHAR( t1.created_time, 'dd/MM/yyyy HH24:mi' ) createdTime
FROM open_pm.sr t1
INNER JOIN open_pm.sr_catalog t2
ON ( t1.service_array = t2.service_array
AND t1.service_group  = t2.service_group
AND t1.service_id     = t2.service_id )
INNER JOIN open_pm.sr_config t3
ON t1.status        = t3.config_code
AND t3.config_group = 'STATUS'
LEFT JOIN common_gnoc.users t4
ON t1.created_user = t4.username
LEFT JOIN common_gnoc.unit t5
ON t4.unit_id = t5.unit_id
INNER JOIN open_pm.sr_config t6
ON t6.config_code   = t2.service_code
WHERE 1             = 1
AND t6.config_group = :configGroup
