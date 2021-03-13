WITH lang_exchange_user_group_cat AS
  (SELECT a.ugcy_id,
    a.ugcy_code,
    a.ugcy_name,
    a.ugcy_system,
    a.ugcy_is_active,
    le2.LEE_VALUE,
    le2.LEE_LOCALE
  FROM OPEN_PM.USER_GROUP_CATEGORY a
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2
  ON a.ugcy_id              = le2.BUSSINESS_ID
  AND le2.APPLIED_SYSTEM    = 2
  AND le2.APPLIED_BUSSINESS = 16
  AND le2.LEE_LOCALE        = :leeLocale
  )
SELECT leugc.ugcy_Id ugcyId,
  leugc.ugcy_Code ugcyCode,
  CASE
    WHEN leugc.LEE_VALUE IS NULL
    THEN TO_CHAR(leugc.ugcy_Name)
    ELSE TO_CHAR(leugc.LEE_VALUE)
  END ugcyName,
  leugc.ugcy_System ugcySystem,
  leugc.ugcy_Is_Active ugcyIsActive
FROM lang_exchange_user_group_cat leugc
WHERE 1               = 1
