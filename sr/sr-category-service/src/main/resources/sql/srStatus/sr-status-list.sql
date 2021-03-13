SELECT
  config_Id configId ,
  config_Group configGroup ,
  config_Code configCode ,
  config_Name configName ,
  status status ,
  created_User createdUser ,
  created_Time createdTime ,
  updated_User updatedUser ,
  updated_Time updatedTime
FROM open_pm.SR_CONFIG
WHERE CONFIG_GROUP = 'STATUS'
AND 1=1
