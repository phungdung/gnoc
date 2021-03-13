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
    AND ITEM_CODE     = :p_system
    ) CAT1
  ON LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 262
    AND ITEM_CODE     = :p_bussiness
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  parent_code AS
  (SELECT
    CASE
      WHEN parent_id IS NULL
      THEN NULL
      ELSE cr_process_id
    END cr_process_id
  FROM cr_process
  WHERE cr_process_id = :p_cr_process_id
  ),
  lst_data AS
  (SELECT cp.cr_process_id crProcessId,
    cp.cr_process_code crProcessCode,
    cp.cr_process_name crProcessName
  FROM cr_process cp
  WHERE level                         = 2
  AND cp.is_active                    = 1
    CONNECT BY prior cp.cr_process_id = cp.parent_id
    START WITH cp.cr_process_id      IN
    (SELECT cr_process_id FROM parent_code
    ) and cp.cr_process_id = :p_cr_process_id
  )
SELECT
cr.crProcessId crProcessId,
cr.crProcessCode crProcessCode,
CASE
      WHEN lle.LEE_VALUE IS NULL
      THEN cr.crProcessName
      ELSE lle.LEE_VALUE
    END crProcessName
FROM lst_data cr
Left join list_language_exchange lle
on cr.crProcessId = lle.BUSSINESS_ID
where 1=1
