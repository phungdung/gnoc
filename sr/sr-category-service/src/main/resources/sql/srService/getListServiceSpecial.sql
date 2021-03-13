SELECT t4.CONFIG_ID configId ,
  t4.CONFIG_CODE configCode ,
  t4.CONFIG_NAME configName ,
  t4.CREATED_USER createdUser ,
  TO_CHAR (t4.CREATED_TIME, 'dd/MM/yyyy HH24:mi:ss') createdTime,
  t4.UPDATED_USER updatedUser,
  TO_CHAR(t4.UPDATED_TIME,'dd/MM/yyyy HH24:mi:ss' )updatedTime,
  t4.STATUS status,
  t4.COUNTRY country
FROM OPEN_PM.SR_CONFIG t4
WHERE 1             = 1
AND t4.CONFIG_GROUP = 'DICH_VU_DAC_BIET'
AND t4.STATUS       = 'A'
