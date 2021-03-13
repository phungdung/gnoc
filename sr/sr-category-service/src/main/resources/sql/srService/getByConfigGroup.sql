SELECT a.CONFIG_ID configId ,
  a.CONFIG_GROUP configGroup ,
  a.CONFIG_CODE configCode ,
  CASE
    WHEN b.LEE_LOCALE = :locale
    THEN (
      CASE
        WHEN b.LEE_VALUE IS NOT NULL
        THEN b.LEE_VALUE
        ELSE CAST(a.CONFIG_NAME AS NVARCHAR2(2000))
      END )
    ELSE CAST(a.CONFIG_NAME AS NVARCHAR2(2000))
  END configName ,
  a.CREATED_USER createdUser ,
  a.CREATED_TIME createdTime ,
  a.UPDATED_USER updatedUser ,
  a.UPDATED_TIME updatedTime ,
  a.STATUS status ,
  a.PARENT_GROUP parentGroup ,
  a.PARENT_CODE parentCode ,
  a.COUNTRY country ,
  a.AUTO_MATION automation
FROM OPEN_PM.SR_CONFIG a
LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE b
ON a.CONFIG_ID          = b.BUSSINESS_ID
AND b.APPLIED_SYSTEM    = 2
AND b.APPLIED_BUSSINESS = 20
WHERE 1                 = 1
AND STATUS              = 'A'
