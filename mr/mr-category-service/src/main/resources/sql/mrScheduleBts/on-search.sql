  SELECT T1.SCHEDULE_ID scheduleId,
     T1.MARKET_CODE marketCode,
     T1.AREA_CODE areaCode,
     T1.STATION_CODE stationCode,
     T1.DEVICE_TYPE deviceType,
     T1.SERIAL serial,
     T1.CYCLE cycle,
     T1.MR_CODE mrCode,
     T1.WO_CODE woCode,
     T2.STATUS woStatus,
     T1.IS_WO_ORIGINAL isWoOriginal,
     TO_CHAR(T1.MODIFY_DATE , 'dd/MM/yyyy') modifyDate,
     TO_CHAR(T1.NEXT_DATE_MODIFY, 'dd/MM/yyyy') nextDateModify,
     T1.USER_MANAGER userManager, T1.PROVINCE_CODE provinceCode,
     cl.LOCATION_NAME marketName
     FROM MR_SCHEDULE_BTS T1
    LEFT JOIN WFM.WO T2 ON T1.WO_CODE = T2.WO_CODE
    LEFT JOIN COMMON_GNOC.CAT_LOCATION cl ON (T1.MARKET_CODE  = cl.LOCATION_ID and cl.PARENT_ID is null and cl.status = 1)
     WHERE 1=1 AND (T1.DELETE_FLAG = 0 or T1.DELETE_FLAG is null)
