WITH lst_lang AS
  (SELECT
    CASE
      WHEN l.LEE_VALUE IS NULL
      THEN c.ITEM_NAME
      ELSE l.LEE_VALUE
    END LEE_VALUE,
    c.ITEM_CODE ITEM_CODE
  FROM COMMON_GNOC.CAT_ITEM c
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE l
  ON c.ITEM_ID         = l.BUSSINESS_ID
  AND APPLIED_BUSSINESS=3
  AND l.APPLIED_SYSTEM =1
  AND l.LEE_LOCALE     =:lee_Locale
  WHERE c.STATUS       = 1
  AND c.CATEGORY_ID    =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='MR_SUBCATEGORY'
    AND STATUS         = 1
    )
  ORDER BY position,
    NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese' )
  )
SELECT DISTINCT mrDeviceHisId ,
  marketCode ,
  arrayCodeStr,
  cl.location_name nationName,
  region,
  title,
  arrayCode ,
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
  cycle,
  importantLevel ,
  procedureId ,
  procedureName ,
--   marketName ,
  arrayName ,
  CASE
    WHEN nodeStatus = 0
    THEN :nodeStatus0
    WHEN nodeStatus = 1
    THEN :nodeStatus1
  END nodeStatus,
  woId ,
  CASE
    WHEN state = 1
    THEN :state1
    WHEN state = 2
    THEN :state2
    WHEN state = 3
    THEN :state3
    WHEN state = 4
    THEN :state4
    WHEN state = 5
    THEN :state5
    WHEN state = 6
    THEN :state6
    WHEN state = 7
    THEN :state7
    WHEN state = 8
    THEN :state8
    WHEN state = 9
    THEN :state9
  END state,
  unitName,
  considerUnitCR,
  considerName,
  responsibleUnitCR,
  reponsibleUnitName,
  TO_CHAR(earliestTime ,'dd/mm/yyyy HH24:mi:ss') earliestTime,
  TO_CHAR(lastestTime ,'dd/mm/yyyy HH24:mi:ss') lastestTime,
  note ,
  mrArrayType,
  mrTypeName
FROM
  (SELECT T1.MR_DEVICE_HIS_ID mrDeviceHisId ,
    T1.MARKET_CODE marketCode ,
    T1.REGION region,
    T1.TITLE title,
    T1.ARRAY_CODE arrayCode ,
    CASE
      WHEN ll.LEE_VALUE IS NULL
      THEN T1.ARRAY_CODE
      ELSE TO_CHAR(ll.LEE_VALUE)
    END arrayCodeStr,
    T1.DEVICE_TYPE deviceType ,
    T1.DEVICE_ID deviceId ,
    T1.DEVICE_CODE deviceCode ,
    T1.DEVICE_NAME deviceName ,
    T1.MR_DATE mrDate,
    T1.MR_CONTENT mrContent ,
    T1.MR_MODE mrMode ,
    T1.MR_TYPE mrType ,
    T1.MR_ID mrId ,
    T1.MR_CODE mrCode ,
    T1.CR_ID crId ,
    cr.CR_NUMBER crNumber ,
    T1.CYCLE cycle,
    T1.IMPORTANT_LEVEL importantLevel ,
    T1.PROCEDURE_ID procedureId ,
    T1.PROCEDURE_NAME procedureName ,
--     T2.MARKET_NAME marketName ,
    T3.ARRAY_NAME arrayName ,
    CASE
      WHEN cr.STATE = 9
      THEN (
        CASE
          WHEN cr.CHANGE_RESPONSIBLE IS NOT NULL
          THEN 1
          ELSE 0
        END)
    END nodeStatus ,
    NULL woId ,
    CASE
      WHEN cr.state         = 9
      AND crHis.action_Code = 25
      AND crHis.return_Code = 43
      THEN '7'
      WHEN cr.state         = 9
      AND crHis.action_Code = 25
      AND crHis.return_Code = 44
      THEN '8'
      WHEN cr.state          = 9
      AND crHis.action_Code  = 10
      AND crHis.return_Code IN
        (SELECT DISTINCT RCCG_ID
        FROM open_pm.return_code_catalog
        WHERE return_category = 10
        AND is_active         = 1
        )
      THEN '9'
      ELSE TO_CHAR(tMR.STATE)
    END state ,
    dvt.UNIT_NAME unitName,
    cr.CONSIDER_UNIT_ID considerUnitCR,
    unit.UNIT_NAME considerName,
    cr.CHANGE_RESPONSIBLE_UNIT responsibleUnitCR,
    unitRes.UNIT_NAME reponsibleUnitName,
    CASE
      WHEN tMR.EARLIEST_TIME IS NULL
      THEN cr.EARLIEST_START_TIME
      ELSE tMR.EARLIEST_TIME
    END earliestTime,
    CASE
      WHEN tMR.LASTEST_TIME IS NULL
      THEN crHis.CHANGE_DATE
      ELSE tMR.LASTEST_TIME
    END lastestTime,
    T1.NOTE note,
    'IT' mrArrayType,
    :mrTypeName mrTypeName
  FROM OPEN_PM.MR_SCHEDULE_IT_HIS T1
--   LEFT JOIN MR_CAT_MARKET T2
--   ON T1.MARKET_CODE = T2.COUNTRY
  LEFT JOIN MR_CAT_ARRAY T3
  ON T1.ARRAY_CODE = T3.ARRAY_CODE
  LEFT JOIN OPEN_PM.CR cr
  ON T1.CR_ID = cr.CR_ID
  LEFT JOIN OPEN_PM.CR_HIS crHis
  ON cr.CR_ID      = crHis.CR_ID
  AND crHis.STATUS = 9
  LEFT JOIN OPEN_PM.MR tMR
  ON T1.MR_ID = tMR.MR_ID
  LEFT JOIN common_gnoc.users cu
  ON tMR.CREATE_PERSON_ID = cu.USER_ID
  LEFT JOIN common_gnoc.UNIT dvt
  ON cu.UNIT_ID = dvt.UNIT_ID
  LEFT JOIN COMMON_GNOC.UNIT unit
  ON cr.CONSIDER_UNIT_ID = unit.UNIT_ID
  LEFT JOIN COMMON_GNOC.UNIT unitRes
  ON cr.CHANGE_RESPONSIBLE_UNIT = unitRes.UNIT_ID
  LEFT JOIN OPEN_PM.MR_SCHEDULE_IT mrTel
  ON T1.MR_ID = mrTel.MR_ID
  LEFT JOIN OPEN_PM.MR_CFG_PROCEDURE_IT_SOFT mrCfg
  ON T1.PROCEDURE_ID = mrCfg.PROCEDURE_ID
  LEFT JOIN lst_lang ll
  ON ll.ITEM_CODE = T1.ARRAY_CODE
  WHERE 1         =1
  )
LEFT JOIN common_gnoc.cat_location cl
ON cl.location_id = marketCode
WHERE 1           =1
AND mrMode        = 'H'
