WITH tmp_data AS
  (SELECT G.CONFIG_ID configId ,
    G.CONFIG_CODE configCode ,
    G.CONFIG_NAME configName ,
    A.CONFIG_CODE parentCode ,
    G.PARENT_GROUP parentGroup ,
    G.COUNTRY country
  FROM OPEN_PM.SR_CONFIG G,
    OPEN_PM.SR_CONFIG A
  WHERE G.CONFIG_GROUP = 'SERVICE_GROUP'
  AND G.PARENT_GROUP   = 'SERVICE_ARRAY'
  AND G.PARENT_CODE    = A.CONFIG_CODE
  AND A.CONFIG_GROUP   = 'SERVICE_ARRAY'
  AND G.STATUS         = 'A'
  AND A.STATUS         = 'A'
  )
SELECT a.configId ,
  a.configCode ,
  CASE
    WHEN b.LEE_LOCALE =:p_leeLocale
    THEN (
      CASE
        WHEN b.LEE_VALUE IS NOT NULL
        THEN b.LEE_VALUE
        ELSE CAST(a.CONFIGNAME AS NVARCHAR2(2000))
      END )
    ELSE CAST(a.CONFIGNAME AS NVARCHAR2(2000))
  END configName,
  a.parentCode ,
  a.parentGroup ,
  a.country
FROM tmp_data a
LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE b
ON a.CONFIGID           = b.BUSSINESS_ID
AND b.APPLIED_SYSTEM    = 2
AND b.APPLIED_BUSSINESS = 20
WHERE 1                 = 1
