WITH tmp_data AS
  (SELECT CONFIG_DES configName,
    CONFIG_ID configId
  FROM
    (SELECT CONFIG_DES,
      CONFIG_ID
    FROM OPEN_PM.SR_CONFIG2
    WHERE STATUS        = 'A'
    AND config_group    = :configGroup
    AND (NEXT_STATUS   IS NULL
    OR NEXT_STATUS      = :nextStatus)
    AND (SERVICE_ARRAY IS NULL
    OR SERVICE_ARRAY    = :serviceArray)
    AND (SERVICE_GROUP IS NULL
    OR SERVICE_GROUP    = :serviceGroup)
    AND (SERVICE_CODE  IS NULL
    OR SERVICE_CODE     = :serviceCode)
    )
  WHERE ROWNUM = 1
  )
SELECT a.configId,
  CASE
    WHEN b.LEE_LOCALE = :locale
    THEN (
      CASE
        WHEN b.LEE_VALUE IS NOT NULL
        THEN b.LEE_VALUE
        ELSE CAST(a.CONFIGNAME AS NVARCHAR2(2000))
      END )
    ELSE CAST(a.CONFIGNAME AS NVARCHAR2(2000))
  END configName
FROM tmp_data a
LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE b
ON a.CONFIGID           = b.BUSSINESS_ID
AND b.APPLIED_SYSTEM    = 2
AND b.APPLIED_BUSSINESS = 22
