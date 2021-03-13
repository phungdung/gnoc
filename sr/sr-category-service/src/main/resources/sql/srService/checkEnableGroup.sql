SELECT t3.CONFIG_ID configId,
  t3.CONFIG_CODE configCode ,
  t3.CONFIG_NAME configName,
  t3.CREATED_USER createdUser,
  TO_CHAR(t3.CREATED_TIME,'dd/MM/yyyy HH24:mi:ss') createdTime,
  t3.UPDATED_USER updatedUser,
  TO_CHAR(t3.UPDATED_TIME,'dd/MM/yyyy HH24:mi:ss' )updatedTime,
  t2.CONFIG_NAME parentName ,
  t3.PARENT_CODE parentCode ,
  t3.STATUS status
FROM
  (SELECT *
  FROM OPEN_PM.SR_CONFIG
  WHERE CONFIG_GROUP='SERVICE_ARRAY'
  AND STATUS        = 'A'
  ) t2
INNER JOIN
  (SELECT *
  FROM OPEN_PM.SR_CONFIG
  WHERE CONFIG_GROUP='SERVICE_GROUP'
  AND STATUS        = 'A'
  ) t3
ON t2.CONFIG_CODE = t3.PARENT_CODE
WHERE 1           = 1
