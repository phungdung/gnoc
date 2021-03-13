SELECT  t.MARKET_CODE marketCode,
        t.ARRAY_CODE arrayCode,
        t.DEVICE_TYPE deviceType,
        t.DEVICE_ID deviceId,
        t.DEVICE_CODE deviceCode,
        t.DEVICE_NAME deviceName,
        to_char(t.LAST_DATE, 'dd/MM/yyyy') mrDate,
        p.MR_CONTENT_ID mrContent,
        t.MR_ID mrId,
        m.MR_TYPE mrType,
        m.MR_CODE mrCode,
        m.STATE mrState,
        t.CR_ID crId,
        c.CR_NUMBER crNumber,
        t.PROCEDURE_ID procedureId,
        p.PROCEDURE_NAME procedureName,
        t.NETWORK_TYPE networkType,
        p.CYCLE cycle,
        t.REGION region,
        t.MR_COMMENT title,
        t.SCHEDULE_ID scheduleId
FROM    MR_SCHEDULE_TEL t
        LEFT JOIN MR m ON t.MR_ID = m.MR_ID
        LEFT JOIN CR c ON t.CR_ID = c.CR_ID
        JOIN MR_CFG_PROCEDURE_TEL p ON t.PROCEDURE_ID = p.PROCEDURE_ID
WHERE   1 = 1
