SELECT DISTINCT
  b3.config_code configCode,
  b3.config_name configName
FROM OPEN_PM.sr_catalog b1
INNER JOIN OPEN_PM.sr_config b2
ON b1.service_code    = b2.config_code
INNER JOIN OPEN_PM.sr_config b3
ON b1.service_group = b3.config_code
WHERE b2.config_group = :configGroup
and b3.config_group='SERVICE_GROUP'
