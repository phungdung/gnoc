WITH lang_exchange_work_log_cat AS
  (SELECT a.WLAY_ID,
    a.WLAY_TYPE,
    a.WLAY_CODE,
    a.WLAY_NAME,
    a.WLAY_IS_ACTIVE,
    le2.LEE_VALUE,
    le2.LEE_LOCALE
  FROM OPEN_PM.WORK_LOG_CATEGORY a
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2
  ON a.WLAY_ID              = le2.BUSSINESS_ID
  AND le2.APPLIED_SYSTEM    = 2
  AND le2.APPLIED_BUSSINESS = 17
  AND le2.LEE_LOCALE        = :leeLocale
  )
SELECT lewlc.WLAY_ID wlayId,
  lewlc.WLAY_TYPE wlayType,
  CASE
    WHEN lewlc.LEE_VALUE IS NULL
    THEN TO_CHAR(lewlc.WLAY_NAME)
    ELSE TO_CHAR(lewlc.LEE_VALUE)
  END wlayName,
  lewlc.WLAY_CODE wlayCode,
  lewlc.WLAY_IS_ACTIVE wlayIsActive
FROM lang_exchange_work_log_cat lewlc
WHERE 1               = 1
