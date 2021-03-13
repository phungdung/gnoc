WITH list_language_scope AS
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
  list_language_impactsegment AS
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
    AND ITEM_CODE     = 'OPEN_PM.IMPACT_SEGMENT'
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  list_result AS
  (SELECT a.CMNOSE_ID,
    a.cmse_id ,
    b.cmse_code ,
    CASE
      WHEN lls.LEE_VALUE IS NULL
      THEN b.cmse_name
      ELSE lls.LEE_VALUE
    END cmse_name,
    a.unit_id ,
    c.unit_name ,
    c.unit_code ,
    a.CR_TYPE ,
    CASE
      WHEN lli.LEE_VALUE IS NULL
      THEN d.IMPACT_SEGMENT_NAME
      ELSE lli.LEE_VALUE
    END IMPACT_SEGMENT_NAME
  FROM open_pm.cr_manager_units_of_scope a
  LEFT JOIN open_pm.cr_manager_scope b
  ON a.cmse_id = b.cmse_id
  LEFT JOIN common_gnoc.unit c
  ON a.unit_id=c.unit_id
  LEFT JOIN OPEN_PM.IMPACT_SEGMENT d
  ON a.CR_TYPE = d.IMPACT_SEGMENT_ID
  LEFT JOIN list_language_scope lls
  ON a.cmse_id = lls.BUSSINESS_ID
  LEFT JOIN list_language_impactsegment lli
  ON a.CR_TYPE      = lli.BUSSINESS_ID
  WHERE b.is_active =1
  AND c.status      =1
  )
SELECT ls.CMNOSE_ID cmnoseId ,
  ls.cmse_id cmseId ,
  ls.cmse_code cmseCode ,
  ls.cmse_name cmseName ,
  ls.unit_id unitId ,
  ls.unit_code unitCode ,
  ls.unit_name unitName ,
  ls.CR_TYPE crTypeId ,
  ls.IMPACT_SEGMENT_NAME crTypeName
FROM list_result ls
WHERE 1=1
