WITH list_language_role AS
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
    AND ITEM_CODE     = 'OPEN_PM.CR_MANAGER_ROLE'
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  list_language_scope AS
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
    AND ITEM_CODE     = 'OPEN_PM.CR_MANAGER_SCOPE'
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  list_result AS
  (SELECT a.CMSORS_ID ,
    a.cmre_id ,
    b.cmre_code ,
    CASE
      WHEN llx1.LEE_VALUE IS NULL
      THEN b.cmre_name
      ELSE llx1.LEE_VALUE
    END cmre_name,
    a.cmse_id ,
    c.cmse_code ,
    CASE
      WHEN llx2.LEE_VALUE IS NULL
      THEN c.cmse_name
      ELSE llx2.LEE_VALUE
    END cmse_name
  FROM open_pm.cr_manager_scopes_of_roles a
  INNER JOIN open_pm.cr_manager_role b
  ON b.cmre_id = a.cmre_id
  INNER JOIN open_pm.cr_manager_scope c
  ON c.cmse_id = a.cmse_id
  LEFT JOIN list_language_role llx1
  ON a.cmre_id = llx1.BUSSINESS_ID
  LEFT JOIN list_language_scope llx2
  ON a.cmse_id     = llx2.BUSSINESS_ID
  WHERE b.is_active=1
  AND c.is_active  =1
  )
SELECT ls.CMSORS_ID cmsorsId,
  ls.cmre_id cmreId,
  ls.cmre_code cmreCode,
  ls.cmre_name cmreName,
  ls.cmse_id cmseId,
  ls.cmse_code cmseCode,
  ls.cmse_name cmseName
FROM list_result ls
WHERE 1=1
