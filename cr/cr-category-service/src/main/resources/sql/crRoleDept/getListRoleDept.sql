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
  AND a.applied_bussiness = 5
  AND lower(a.lee_locale) LIKE lower(:leeLocale)
  ),
  list_search AS
  (SELECT ut.unit_id unitId,
    ut.parent_unit_id parentUnitId,
    CASE
      WHEN ut.unit_code IS NULL
      THEN ''
      ELSE TO_CHAR(ut.unit_code
        || ' ('
        || ut.unit_name
        || ')')
    END AS unitName,
    parentUt.unit_code parentName,
    re.CMRE_NAME cmreName,
    re.CMRE_CODE cmreCode,
    re.cmre_Id cmreId,
    cmrout.cmrout_Id cmroutId
  FROM open_pm.cr_manager_roles_of_unit cmrout
  LEFT JOIN common_gnoc.unit ut
  ON cmrout.unit_id = ut.unit_id
  LEFT JOIN open_pm.cr_manager_role re
  ON cmrout.cmre_id = re.cmre_id
  LEFT JOIN common_gnoc.unit parentUt
  ON ut.parent_unit_id = parentUt.unit_id
  WHERE 1              =1
  ),
  list_result AS
  (SELECT a.unitId,
    a.parentUnitId,
    a.unitName,
    a.parentName,
    a.cmreId,
    a.cmreCode,
    a.cmroutId,
    CASE
      WHEN llx.lee_value IS NULL
      THEN a.cmreName
      ELSE llx.lee_value
    END cmreName
  FROM list_search a
  LEFT JOIN list_language_exchange llx
  ON a.cmreId = llx.bussiness_id
  )
SELECT b.cmreId,
  b.cmreName,
  b.cmreCode,
  b.cmroutId,
  b.unitId,
  b.unitName,
  b.parentName,
  b.parentUnitId
FROM list_result b
  ) re
WHERE 1=1
