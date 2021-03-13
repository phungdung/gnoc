WITH list_language_exchange AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  WHERE LE.APPLIED_SYSTEM  = :applied_system
  AND LE.APPLIED_BUSSINESS = :bussiness
  AND LE.LEE_LOCALE        = :p_leeLocale
  ),
  list_result AS
  (SELECT cfg.cmse_id valueStr,
    cfg.cmse_code secondValue,
    CASE
      WHEN llx.LEE_VALUE IS NULL
      THEN cfg.cmse_name
      ELSE llx.LEE_VALUE
    END displayStr
  FROM OPEN_PM.cr_manager_scope cfg
  LEFT JOIN list_language_exchange llx
  ON cfg.cmse_id      = llx.BUSSINESS_ID
  WHERE cfg.is_active = 1
  AND cmse_id        IN
    (SELECT scope_id
    FROM OPEN_PM.v_manage_cr_config
    WHERE manage_unit = :manage_unit
    AND IS_SCHEDULE_CR_EMERGENCY = 1
    )
  )
SELECT valueStr, secondValue, displayStr FROM list_result ORDER BY displayStr
