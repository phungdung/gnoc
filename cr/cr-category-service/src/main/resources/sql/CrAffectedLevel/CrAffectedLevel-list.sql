WITH list_language_exchange AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 263
    AND ITEM_CODE     = 'OPEN_PM'
    ) CAT1
  ON LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 262
    AND ITEM_CODE     = 'OPEN_PM.AFFECTED_LEVEL'
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  list_result AS
  (SELECT AL.affected_level_id,
    AL.affected_level_code,
    CASE
      WHEN llx.LEE_VALUE IS NULL
      THEN AL.affected_level_name
      ELSE llx.LEE_VALUE
    END affected_level_name,
    AL.two_approve_level,
    AL.applied_system,
    AL.is_active
  FROM OPEN_PM.AFFECTED_LEVEL AL
  LEFT JOIN list_language_exchange llx
  ON AL.AFFECTED_LEVEL_ID = llx.BUSSINESS_ID
  )
SELECT affected_level_id affectedLevelId,
  affected_level_code affectedLevelCode,
  affected_level_name affectedLevelName,
  two_approve_level twoApproveLevel,
  applied_system appliedSystem
FROM list_result
WHERE is_active=1
