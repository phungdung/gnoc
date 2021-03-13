SELECT t3.CONFIG_ID configId,
  t3.CONFIG_GROUP configGroup,
  t3.CONFIG_CODE configCode ,
  t3.CONFIG_NAME configName,
  t3.CREATED_USER createdUser,
  TO_CHAR(t3.CREATED_TIME,'dd/MM/yyyy HH24:mi:ss') createdTime,
  t2.CONFIG_NAME parentName ,
  t3.PARENT_CODE parentCode ,
  t3.STATUS status,
  t3.PARENT_GROUP parentGroup,
  t3.COUNTRY country
FROM
  (SELECT *
  FROM OPEN_PM.SR_CONFIG
  WHERE CONFIG_GROUP='SERVICE_ARRAY'
  AND STATUS        = 'A'
  ) t2
INNER JOIN
  (SELECT * FROM OPEN_PM.SR_CONFIG WHERE CONFIG_GROUP='SERVICE_GROUP'
  ) t3
ON t2.CONFIG_CODE          = t3.PARENT_CODE
WHERE 1           = 1
-- --AND (LOWER(t3.CONFIG_CODE) like :configCode ESCAPE '\'
-- --OR LOWER(T3.CONFIG_NAME) like :configName ESCAPE '\')
-- AND (lower(T3.CONFIG_CODE) like 'vo tuyen_tỉnh_06'
-- OR lower(T3.CONFIG_NAME)   like 'nâng cấp trạm')
