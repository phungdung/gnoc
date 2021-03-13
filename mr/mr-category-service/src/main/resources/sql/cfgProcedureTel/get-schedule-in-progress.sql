SELECT
       T1.SCHEDULE_ID scheduleId,
       T1.MARKET_CODE marketCode ,
       T1.ARRAY_CODE arrayCode,
       T1.DEVICE_TYPE deviceType ,
       T1.DEVICE_ID deviceId,
       T1.DEVICE_CODE deviceCode,
       T1.DEVICE_NAME deviceName ,
       T1.PROCEDURE_ID procedureId,
       T1.NEXT_DATE_MODIFY nextDateModify ,
       T1.REGION region,
       T1.MR_ID mrId,
       T1.MR_CONFIRM mrConfirm ,
       T1.MR_COMMENT mrComment,
       T1.NETWORK_TYPE networkType ,
       T1.CR_ID crId,
       T2.MR_CONTENT_ID mrContentId,
       T2.PROCEDURE_NAME procedureName,
       T3.MR_TYPE mrType,
       T3.MR_CODE mrCode,
       T2.TYPE_CR typeCr,
       T2.ARRAY_ACTION_NAME arrayActionName,
       T1.WO_ID woId,
       T2.CYCLE_TYPE cycleType,
       T1.MR_HARD_CYCLE mrHardCycle,
       T2.CYCLE cycle
FROM MR_SCHEDULE_TEL T1
       left join MR_CFG_PROCEDURE_TEL T2 ON T1.PROCEDURE_ID = T2.PROCEDURE_ID
       left join MR T3 ON T1.MR_ID = T3.MR_ID
       left JOIN MR_DEVICE T4 ON T1.DEVICE_ID = T4.DEVICE_ID
WHERE 1 = 1
