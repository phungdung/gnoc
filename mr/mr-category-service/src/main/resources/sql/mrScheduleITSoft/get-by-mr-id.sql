SELECT T1.SCHEDULE_ID scheduleId ,
  T1.MARKET_CODE marketCode ,
  T1.ARRAY_CODE arrayCode ,
  T1.REGION region ,
  T1.DEVICE_TYPE deviceType ,
  T1.DEVICE_ID deviceId ,
  T1.DEVICE_CODE deviceCode ,
  T1.DEVICE_NAME deviceName ,
  T1.PROCEDURE_ID procedureId ,
  TO_CHAR(T1.LAST_DATE, 'dd/MM/yyyy') lastDate ,
  TO_CHAR(T1.UPTIME_DATE, 'dd/MM/yyyy') uptimeDate ,
  TO_CHAR(T1.NEXT_DATE, 'dd/MM/yyyy') nextDate ,
  TO_CHAR(T1.NEXT_DATE_MODIFY, 'dd/MM/yyyy') nextDateModify ,
  T1.MODIFY_USER modifyUser ,
  TO_CHAR(T1.MODIFY_DATE, 'dd/MM/yyyy') modifyDate ,
  TO_CHAR(T1.UPDATED_DATE, 'dd/MM/yyyy') updatedDate ,
  T1.LEVEL_IMPORTANT levelImportant ,
  T1.MR_ID mrId ,
  T1.PROVIDER_ID providerId ,
  T1.GROUP_CODE groupCode ,
  T2.MR_TYPE mrType ,
  T2.MR_CODE mrCode ,
  T2.CR_ID crId ,
  T3.CR_NUMBER crNumber ,
  T4.PROCEDURE_NAME procedureName ,
  T4.MR_CONTENT_ID mrContentId ,
  T4.MR_MODE mrMode
FROM MR_SCHEDULE_IT T1
LEFT JOIN MR T2
ON T1.MR_ID = T2.MR_ID
LEFT JOIN CR T3
ON T2.CR_ID = T3.CR_ID
LEFT JOIN MR_CFG_PROCEDURE_IT_SOFT T4
ON T1.PROCEDURE_ID = T4.PROCEDURE_ID
WHERE 1            = 1
AND T1.MR_ID       = :mrId
