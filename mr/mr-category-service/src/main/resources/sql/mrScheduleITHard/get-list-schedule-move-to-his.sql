WITH temp_data AS
  (SELECT c.MARKET_CODE ,
    c.OBJECT_ID,
    a.ARRAY_CODE ,
    a.DEVICE_TYPE ,
    a.DEVICE_ID ,
    a.DEVICE_CODE ,
    a.DEVICE_NAME ,
    b.MR_CONTENT_ID ,
    b.MR_TYPE ,
    a.MR_ID ,
    mr.mr_code ,
    mr.state ,
    b.PROCEDURE_ID ,
    b.PROCEDURE_NAME ,
    a.NOTE ,
    c.LEVEL_IMPORTANT ,
    a.REGION ,
    a.CR_ID ,
    b.CYCLE ,
    b.CYCLE_TYPE,
    a.SCHEDULE_ID
  FROM MR_SCHEDULE_IT_HARD a
  LEFT JOIN MR mr
  ON a.MR_ID = mr.MR_ID
  LEFT JOIN MR_CFG_PROCEDURE_IT_HARD b
  ON a.PROCEDURE_ID = b.PROCEDURE_ID
  LEFT JOIN MR_SYN_IT_DEVICES c
  ON a.MARKET_CODE  = c.MARKET_CODE
  AND a.DEVICE_TYPE = c.DEVICE_TYPE
  AND a.DEVICE_ID   = c.OBJECT_ID
  )
SELECT a.MARKET_CODE marketCode ,
  a.OBJECT_ID objectId,
  a.ARRAY_CODE arrayCode,
  a.DEVICE_TYPE deviceType,
  a.DEVICE_ID deviceId,
  a.DEVICE_CODE deviceCode,
  a.DEVICE_NAME deviceName,
  a.MR_CONTENT_ID mrContent,
  a.MR_TYPE mrType,
  a.MR_ID mrId,
  a.mr_code mrCode,
  a.state mrState,
  a.PROCEDURE_ID procedureId,
  a.PROCEDURE_NAME procedureName,
  a.NOTE note,
  a.LEVEL_IMPORTANT importantLevel,
  a.REGION region,
  a.CR_ID crId,
  a.CYCLE cycle,
  a.CYCLE_TYPE cycleType,
  a.SCHEDULE_ID scheduleId
FROM temp_data a
WHERE 1=1
