SELECT DISTINCT b2.user_name userName,
  b3.user_id userId
FROM OPEN_PM.sr_catalog b1
JOIN OPEN_PM.sr_role_user b2
ON b1.execution_unit = b2.unit_id
JOIN common_gnoc.users b3
ON b2.user_name = b3.username
JOIN OPEN_PM.sr_config b4
ON b1.service_code    = b4.config_code
WHERE 1=1
AND b4.config_group   = 'DICH_VU_VSMART'
AND b2.STATUS         = 'A'
