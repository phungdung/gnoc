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
  ),list_cr_process AS (
  select
    cr_process_id,
    cr_process_code,
    cr_process_name,
    is_active,
    parent_id
  from open_pm.cr_process
)
SELECT lcp.cr_process_id,
  lcp.cr_process_code,
  CASE
    WHEN lle.LEE_VALUE IS NULL
    THEN lcp.cr_process_name
    ELSE lle.LEE_VALUE
  END cr_process_name,
  lcp.is_active,
  lcp.parent_id
FROM list_cr_process lcp
LEFT JOIN list_language_exchange lle
ON lcp.cr_process_id = lle.BUSSINESS_ID where 1=1 and lcp.is_active =1
