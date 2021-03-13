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
  list_data AS
  (SELECT cr_process_id ,
    cr_process_name ,
    cr_process_code ,
    parent_id,
    RPAD('--|', (level-1)*3, '--|')
    || cr_process_name AS tree,
    level clevel,
    is_active,
    CONNECT_BY_ROOT cr_process_id                       AS root_id,
    LTRIM(SYS_CONNECT_BY_PATH(cr_process_id, '-'), '-') AS path,
    CONNECT_BY_ISLEAF                                   AS isLEAF,
    cps.impact_type ,
    cps.RISK_LEVEL AS thirdValue
  FROM open_pm.cr_process cps
    START WITH
    (
      cps.parent_id IS NULL
    AND is_active    =1
    )
    CONNECT BY parent_id = PRIOR cr_process_id
  ORDER SIBLINGS BY cr_process_id
  )
SELECT ld.cr_process_id AS valueStr,
  CASE
    WHEN llx.LEE_VALUE IS NULL
    THEN ld.tree
    ELSE RPAD('--|', (clevel-1)*3, '--|')
      || llx.LEE_VALUE
  END displayStr,
  ld.impact_type
  || ','
  || ld.isleaf AS secondValue ,
  thirdValue,
  path,
  clevel
FROM list_data ld
LEFT JOIN list_language_exchange llx
ON ld.cr_process_id     = llx.BUSSINESS_ID
WHERE ld.cr_process_id IN
  (SELECT cps.cr_process_id
  FROM open_pm.cr_process cps
    START WITH
    (
      1,
      cps.cr_process_id
    )
    IN (
