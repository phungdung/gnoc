SELECT DISTINCT b1.service_code serviceCode,
  b1.service_name serviceName
FROM OPEN_PM.sr_catalog b1
INNER JOIN OPEN_PM.sr_config b2
ON b1.service_code    = b2.config_code
WHERE b2.config_group = :configGroup
