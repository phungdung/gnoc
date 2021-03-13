WITH
  list_language_exchange AS
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
  parent_list AS
  ( SELECT DISTINCT cr_process_id
  FROM CR_PROCESS
  WHERE parent_id IS NULL
  ),
  list_data AS (
    SELECT a.cr_process_id crProcessId ,
      a.cr_process_code crProcessCode,
      a.cr_process_name crProcessName,
      level levelProcess
    FROM CR_PROCESS a
    WHERE level <= 2
     AND is_active = 1
      CONNECT BY prior cr_process_id = parent_id
      START WITH cr_process_id      IN
      (SELECT cr_process_id FROM parent_list)
  )
  select
    ld.crProcessId crProcessId,
    ld.crProcessCode crProcessCode,
    CASE
        WHEN (ld.levelProcess = 2 and lle.LEE_VALUE is not null) THEN '--|' || lle.LEE_VALUE
        WHEN (ld.levelProcess = 2 and lle.LEE_VALUE is null) THEN '--|' || ld.crProcessName
        WHEN lle.LEE_VALUE is null THEN ld.crProcessName
        ELSE lle.LEE_VALUE
    END crProcessName
  from list_data ld
  LEFT JOIN list_language_exchange lle on lle.BUSSINESS_ID = ld.crProcessId
  where 1=1
