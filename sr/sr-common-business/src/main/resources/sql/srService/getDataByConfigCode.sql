SELECT CONFIG_GROUP configGroup,
  CONFIG_CODE configCode,
  CONFIG_NAME configName,
  AUTO_MATION automation
FROM OPEN_PM.sr_config
WHERE CONFIG_GROUP =:p_config_group
AND CONFIG_CODE    = :p_config_code
