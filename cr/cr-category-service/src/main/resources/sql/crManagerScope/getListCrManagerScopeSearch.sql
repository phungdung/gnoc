  SELECT *
FROM
  ( WITH list_language_exchange AS
  (SELECT a.lee_id ,
    a.applied_system ,
    a.applied_bussiness ,
    a.bussiness_id ,
    a.bussiness_code ,
    a.lee_locale ,
    a.lee_value
  FROM common_gnoc.language_exchange a
  WHERE a.applied_system  = 2
  AND a.applied_bussiness = 6
  AND lower(a.lee_locale) LIKE lower(:leeLocale)
  ),
  list_search AS
    (select a.CMSE_ID cmseId,a.CMSE_CODE cmseCode,a.CMSE_NAME cmseName,a.IS_ACTIVE isActive,a.DESCRIPTION description from open_pm.CR_MANAGER_SCOPE  a where 1=1 and a.IS_ACTIVE=1
  ),
  list_result AS
  (SELECT a.cmseId,
    a.cmseCode,
    a.isActive,
    a.description,
    CASE
      WHEN llx.lee_value IS NULL
      THEN a.cmseName
      ELSE llx.lee_value
    END cmseName
  FROM list_search a
  LEFT JOIN list_language_exchange llx
  ON a.cmseId = llx.bussiness_id
  )
SELECT b.cmseId,
    b.cmseCode,
    b.isActive,
    b.description,
    b.cmseName
FROM list_result b
  ) re
WHERE 1=1
