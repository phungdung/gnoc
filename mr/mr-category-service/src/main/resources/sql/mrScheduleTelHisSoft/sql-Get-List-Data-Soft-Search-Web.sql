SELECT  DISTINCT h.MR_DEVICE_HIS_ID mrDeviceHisId,
        h.MARKET_CODE marketCode,
        h.REGION region,
        h.ARRAY_CODE arrayCode,
        h.DEVICE_TYPE deviceType,
        h.DEVICE_ID deviceId,
        h.DEVICE_CODE deviceCode,
        h.DEVICE_NAME deviceName,
        TO_CHAR(h.MR_DATE, 'dd/MM/yyyy') mrDate,
        h.MR_CONTENT mrContent,
        h.MR_MODE mrMode,
        h.MR_TYPE mrType,
        h.MR_ID mrId,
        h.MR_CODE mrCode,
        h.CR_ID crId,
        cr.CR_NUMBER crNumber,
        h.CYCLE cycle,
        h.IMPORTANT_LEVEL importantLevel,
        h.PROCEDURE_ID procedureId,
        h.PROCEDURE_NAME procedureName,
        CASE
          WHEN cr.STATE = 9
          THEN (CASE
                  WHEN cr.CHANGE_RESPONSIBLE IS NOT NULL
                    THEN 1
                  ELSE 0
                END)
        END nodeStatus,
        n.WO_ID woId,
        CASE
          WHEN cr.state = 9 AND ch.action_Code = 25 AND ch.return_Code = 43
            THEN '7'
          WHEN cr.state = 9 AND ch.action_Code = 25 AND ch.return_Code = 44
            THEN '8'
          WHEN cr.state = 9 AND ch.action_Code = 10 AND ch.return_Code IN (SELECT DISTINCT RCCG_ID
                                                                            FROM open_pm.return_code_catalog
                                                                            WHERE return_category = 10
                                                                            AND is_active = 1)
            THEN '9'
          ELSE TO_CHAR(mr.STATE)
        END state,
        un.UNIT_ID unitName,
        un.UNIT_NAME unitCreateMr,
        cr.CONSIDER_UNIT_ID considerUnitCR,
        cn.UNIT_NAME considerName,
        cr.CHANGE_RESPONSIBLE_UNIT responsibleUnitCR,
        rn.UNIT_NAME reponsibleUnitName,
        CASE
          WHEN mr.EARLIEST_TIME IS NULL
            THEN TO_DATE(to_char(cr.EARLIEST_START_TIME ,'dd/mm/yyyy HH24:mi:ss'),'dd/mm/yyyy HH24:mi:ss')
          ELSE TO_DATE(to_char(mr.EARLIEST_TIME ,'dd/mm/yyyy HH24:mi:ss'),'dd/mm/yyyy HH24:mi:ss')
        END earliestTime,
        CASE
          WHEN mr.LASTEST_TIME IS NULL
            THEN TO_DATE(to_char(ch.CHANGE_DATE ,'dd/mm/yyyy HH24:mi:ss'),'dd/mm/yyyy HH24:mi:ss')
          ELSE TO_DATE(to_char(mr.LASTEST_TIME,'dd/mm/yyyy HH24:mi:ss'),'dd/mm/yyyy HH24:mi:ss')
        END lastestTime,
        mrTel.MR_COMMENT mrComment,
        mr.MR_TITLE title,
        h.NOTE note,
        'TEL' mrArrayType
FROM    OPEN_PM.MR_SCHEDULE_TEL_HIS h
        LEFT JOIN OPEN_PM.MR mr                 ON h.MR_ID = mr.MR_ID
        LEFT JOIN OPEN_PM.CR cr                 ON h.CR_ID = cr.CR_ID
        LEFT JOIN OPEN_PM.CR_HIS ch             ON cr.CR_ID = ch.CR_ID and ch.STATUS = 9
        LEFT JOIN common_gnoc.users us          ON mr.CREATE_PERSON_ID = us.USER_ID
        LEFT JOIN common_gnoc.UNIT un           ON us.UNIT_ID = un.UNIT_ID
        LEFT JOIN COMMON_GNOC.UNIT cn           ON cr.CONSIDER_UNIT_ID = cn.UNIT_ID
        LEFT JOIN COMMON_GNOC.UNIT rn           ON cr.CHANGE_RESPONSIBLE_UNIT = rn.UNIT_ID
        LEFT JOIN OPEN_PM.MR_SCHEDULE_TEL mrTel ON h.MR_ID = mrTel.MR_ID
        LEFT JOIN MR_NODES n                    ON h.MR_ID = n.MR_ID AND h.DEVICE_CODE = n.NODE_CODE
WHERE   1 = 1
AND     h.MR_MODE = 'S'
