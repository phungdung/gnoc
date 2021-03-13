WITH lang_exchange_priority AS
  (SELECT p1.PRIORITY_ID,
    p1.PRIORITY_NAME,
    p1.PRIORITY_CODE,
    le1.LEE_VALUE,
    le1.LEE_LOCALE
  FROM WFM.WO_PRIORITY p1
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le1
  ON p1.PRIORITY_ID         = le1.BUSSINESS_ID
  AND le1.APPLIED_SYSTEM    = 4
  AND le1.APPLIED_BUSSINESS = 3
  AND le1.LEE_LOCALE        = :leeLocale
  ),
  lang_exchange_wo_type AS
  (SELECT t1.WO_TYPE_ID,
    t1.WO_TYPE_CODE,
    t1.WO_TYPE_NAME,
    t1.TIME_OVER,
    le2.LEE_VALUE,
    le2.LEE_LOCALE
  FROM WFM.WO_TYPE t1
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2
  ON t1.WO_TYPE_ID          = le2.BUSSINESS_ID
  AND le2.APPLIED_SYSTEM    = 4
  AND le2.APPLIED_BUSSINESS = 1
  AND le2.LEE_LOCALE        = :leeLocale
  ),
  lang_exchange_wo_parent_type AS
  (SELECT t2.WO_TYPE_ID,
    t2.WO_TYPE_CODE,
    t2.WO_TYPE_NAME,
    t2.TIME_OVER,
    le3.LEE_VALUE,
    le3.LEE_LOCALE
  FROM WFM.WO_TYPE t2
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le3
  ON t2.WO_TYPE_ID          = le3.BUSSINESS_ID
  AND le3.APPLIED_SYSTEM    = 4
  AND le3.APPLIED_BUSSINESS = 1
  AND le3.LEE_LOCALE        = :leeLocale
  )
SELECT t.ID id,
  t.NAME name,
  t.WO_CONTENT woContent,
  t.WO_TYPE woType,
  CASE
    WHEN lw.LEE_VALUE IS NULL
    THEN TO_CHAR(lw.WO_TYPE_NAME)
    ELSE TO_CHAR(lw.LEE_VALUE)
  END woTypeName,
  t.WO_PARENT_TYPE woParentType,
  CASE
    WHEN lw2.LEE_VALUE IS NULL
    THEN TO_CHAR(lw2.WO_TYPE_NAME)
    ELSE TO_CHAR(lw2.LEE_VALUE)
  END woParentTypeName,
  t.WO_PRIORITY woPriority,
  CASE
    WHEN p.LEE_VALUE IS NULL
    THEN TO_CHAR(p.PRIORITY_NAME)
    ELSE TO_CHAR(p.LEE_VALUE)
  END woPriorityName,
  t.FILE_ID fileId,
  t.CD_ID cdId,
  c.WO_GROUP_NAME cdName,
  t.DELTA_TIME1 deltaTime1,
  t.DELTA_TIME2 deltaTime2
FROM COMMON_GNOC.WO_TEST_SERVICE_CONF t
LEFT JOIN lang_exchange_priority p
ON t.WO_PRIORITY = p.PRIORITY_ID
LEFT JOIN lang_exchange_wo_type lw
ON t.WO_TYPE = lw.WO_TYPE_ID
LEFT JOIN lang_exchange_wo_parent_type lw2
ON t.WO_PARENT_TYPE = lw2.WO_TYPE_ID
LEFT JOIN WFM.wo_cd_group c
ON t.CD_ID = c.WO_GROUP_ID
WHERE 1    = 1
