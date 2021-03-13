SELECT CONFIG_ID configId ,
  CONFIG_CODE configCode ,
  STATUS status ,
  NEXT_STATUS nextStatus ,
  SERVICE_ARRAY serviceArray ,
  SERVICE_GROUP serviceGroup ,
  CURRENT_STATUS currentStatus ,
  CONFIG_GROUP configGroup ,
  CONFIG_DES configDes ,
  SERVICE_CODE serviceCode
FROM OPEN_PM.SR_CONFIG2
WHERE 1           = 1
AND STATUS        = 'A'
AND CONFIG_GROUP  = :configGroup
