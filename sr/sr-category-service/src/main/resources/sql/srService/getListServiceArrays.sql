SELECT T1.CONFIG_ID configId ,
  T1.CONFIG_CODE configCode,
  T1.CONFIG_NAME configName ,
  T1.CREATED_USER createdUser ,
  TO_CHAR(T1.CREATED_TIME,'dd/MM/yyyy HH24:mi:ss') createdTime ,
  T1.UPDATED_USER updatedUser ,
  TO_CHAR(T1.UPDATED_TIME,'dd/MM/yyyy HH24:mi:ss' )updatedTime ,
  T1.STATUS status ,
  T1.COUNTRY country ,
  T1.AUTO_MATION automation
FROM OPEN_PM.SR_CONFIG T1
WHERE 1             = 1
AND T1.CONFIG_GROUP = 'SERVICE_ARRAY'
AND T1.STATUS       = 'A'
