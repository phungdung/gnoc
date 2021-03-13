WITH 
list_language_exchange AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = :applied_system and LE.APPLIED_BUSSINESS = :bussiness and LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (

  SELECT mrDeviceHisId ,
  marketCode ,
  region,
--  title,
	case
      when llx.LEE_VALUE is null
      then to_char(arrayCode)
      else to_char(llx.LEE_VALUE) end arrayCode,
  deviceType ,
  deviceId ,
  deviceCode ,
  deviceName ,
  TO_CHAR(mrDate, 'dd/MM/yyyy') mrDate ,
  mrContent ,
  mrMode ,
  mrType ,
  mrId ,
  mrCode ,
  crId ,
  crNumber ,
  importantLevel ,
  procedureId ,
  procedureName ,
  marketName ,
  arrayName ,
  nodeStatus ,
  woId ,
  considerUnitCR,
  responsibleUnitCR,
  earliestTime,
  lastestTime,
  note,
--  title,
  mrArrayType
  FROM (SELECT DISTINCT mrDeviceHisId ,
  marketCode ,
  region,
--  title,
  arrayCode ,
  deviceType ,
  deviceId ,
  deviceCode ,
  deviceName ,
  MR_DATE mrDate ,
  mrContent ,
  mrMode ,
  mrType ,
  mrId ,
  mrCode ,
  crId ,
  crNumber ,
  importantLevel ,
  procedureId ,
  procedureName ,
  marketName ,
  arrayName ,
  nodeStatus ,
  woId ,
  considerUnitCR,
  responsibleUnitCR,
  earliestTime,
  lastestTime,
  note,
--  title,
  mrArrayType
FROM
  (SELECT T1.MR_DEVICE_HIS_ID mrDeviceHisId ,
    T1.MARKET_CODE marketCode ,
    T1.REGION region,
    T1.TITLE title,
    T1.ARRAY_CODE arrayCode ,
    T1.DEVICE_TYPE deviceType ,
    T1.DEVICE_ID deviceId ,
    T1.DEVICE_CODE deviceCode ,
    T1.DEVICE_NAME deviceName ,
    T1.MR_DATE ,
    T1.MR_CONTENT mrContent ,
    T1.MR_MODE mrMode ,
    T1.MR_TYPE mrType ,
    T1.MR_ID mrId ,
    T1.MR_CODE mrCode ,
    T1.CR_ID crId ,
    T1.CR_NUMBER crNumber ,
    T1.IMPORTANT_LEVEL importantLevel ,
    T1.PROCEDURE_ID procedureId ,
    T1.PROCEDURE_NAME procedureName ,
    T2.MARKET_NAME marketName ,
    T3.ARRAY_NAME arrayName ,
    CASE
      WHEN tMR.STATE = 6
      THEN (
        CASE
          WHEN wo.FT_ID IS NOT NULL
          THEN 1
          ELSE 0
        END)
      ELSE NULL
    END nodeStatus ,
    NULL woId ,
    cr.CONSIDER_UNIT_ID considerUnitCR,
    cr.CHANGE_RESPONSIBLE_UNIT responsibleUnitCR,
    tMR.EARLIEST_TIME earliestTime,
    tMR.LASTEST_TIME lastestTime,
    T1.NOTE note,
    tMR.MR_TITLE title,
    'IT' mrArrayType
  FROM OPEN_PM.MR_SCHEDULE_HIS T1
  LEFT JOIN MR_CAT_MARKET T2
  ON T1.MARKET_CODE = T2.MARKET_CODE
  LEFT JOIN MR_CAT_ARRAY T3
  ON T1.ARRAY_CODE = T3.ARRAY_CODE
  LEFT JOIN OPEN_PM.CR cr
  ON T1.CR_ID = cr.CR_ID
  LEFT JOIN OPEN_PM.MR tMR
  ON T1.MR_ID = tMR.MR_ID
  LEFT JOIN MR_NODES T4
  ON T1.MR_ID        = T4.MR_ID
  AND T1.DEVICE_CODE = T4.NODE_CODE
  LEFT JOIN WFM.WO wo
  ON T4.WO_ID = wo.WO_ID
  UNION ALL
  SELECT T1.MR_DEVICE_HIS_ID mrDeviceHisId ,
    T1.MARKET_CODE marketCode ,
    T1.REGION region,
    T1.TITLE title,
    T1.ARRAY_CODE arrayCode ,
    T1.DEVICE_TYPE deviceType ,
    T1.DEVICE_ID deviceId ,
    T1.DEVICE_CODE deviceCode ,
    T1.DEVICE_NAME deviceName ,
    T1.MR_DATE ,
    T1.MR_CONTENT mrContent ,
    T1.MR_MODE mrMode ,
    T1.MR_TYPE mrType ,
    T1.MR_ID mrId ,
    T1.MR_CODE mrCode ,
    T1.CR_ID crId ,
    T1.CR_NUMBER crNumber ,
    T1.IMPORTANT_LEVEL importantLevel ,
    T1.PROCEDURE_ID procedureId ,
    T1.PROCEDURE_NAME procedureName ,
    T2.MARKET_NAME marketName ,
    T3.ARRAY_NAME arrayName ,
    CASE
      WHEN tMR.STATE = 6
      THEN (
        CASE
          WHEN wo.FT_ID IS NOT NULL
          THEN 1
          ELSE 0
        END)
      ELSE NULL
    END nodeStatus ,
    T4.WO_ID woId ,
    cr.CONSIDER_UNIT_ID considerUnitCR,
    cr.CHANGE_RESPONSIBLE_UNIT responsibleUnitCR,
    tMR.EARLIEST_TIME earliestTime,
    tMR.LASTEST_TIME lastestTime,
    T1.NOTE note,
    tMR.MR_TITLE title,
    'TEL' mrArrayType
  FROM OPEN_PM.MR_SCHEDULE_TEL_HIS T1
  LEFT JOIN MR_CAT_MARKET T2
  ON T1.MARKET_CODE = T2.MARKET_CODE
  LEFT JOIN MR_CAT_ARRAY T3
  ON T1.ARRAY_CODE = T3.ARRAY_CODE
  LEFT JOIN OPEN_PM.MR tMR
  ON T1.MR_ID = tMR.MR_ID
  LEFT JOIN OPEN_PM.CR cr
  ON T1.CR_ID = cr.CR_ID
  LEFT JOIN MR_NODES T4
  ON T1.MR_ID        = T4.MR_ID
  AND T1.DEVICE_CODE = T4.NODE_CODE
  LEFT JOIN WFM.WO wo
  ON T4.WO_ID = wo.WO_ID
  )
WHERE 1    =1
AND mrMode = 'H') R1
LEFT JOIN COMMON_GNOC.CAT_ITEM R2
ON (R1.Arraycode = R2.Item_value AND CATEGORY_ID IN
  (SELECT CATEGORY_ID
  FROM COMMON_GNOC.CATEGORY
  WHERE CATEGORY_CODE=:categoryCode
  AND STATUS         = 1
  ))
  left join list_language_exchange llx on R2.Item_id = llx.BUSSINESS_ID
)
select
 *
from list_result
WHERE 1=1
