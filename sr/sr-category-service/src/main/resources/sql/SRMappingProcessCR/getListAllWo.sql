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
    AND ITEM_CODE     = 'OPEN_PM.CR_PROCESS'
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  lst_process AS
  (SELECT cr.cr_process_id crProcessId,
    cr.cr_process_code crProcessCode,
    cr.cr_process_name crProcessName
  FROM cr_process cr
  WHERE level                         = 3
  AND cr.is_active                    = 1
    CONNECT BY prior cr.cr_process_id = cr.parent_id
    START WITH cr.parent_id          IS NULL
  )
SELECT
cr_p.crProcessId crProcessId,
cr_p.crProcessCode crProcessCode,
CASE
WHEN lle.LEE_VALUE IS NULL
THEN cr_p.crProcessName
ELSE lle.LEE_VALUE
END crProcessName
FROM lst_process cr_p
LEFT JOIN list_language_exchange lle
  ON cr_p.crProcessId = lle.BUSSINESS_ID
