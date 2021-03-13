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
  AND a.applied_bussiness = 9
  AND lower(a.lee_locale) LIKE lower(:leeLocale)
  ),
  list_search AS
  (SELECT b.group_unit_code groupUnitCode,
    b.group_unit_id groupUnitId,
    b.group_unit_name groupUnitName,
    c.unit_id unitId,
    c.unit_name unitName,
    c.unit_code unitCode,
    a.GROUP_UNIT_DETAIL_ID groupUnitDetailId
  FROM open_pm.GROUP_UNIT_DETAIl a,
    open_pm.GROUP_UNIT b,
    common_gnoc.unit c
  WHERE a.group_unit_id = b.group_unit_id
  AND b.is_active       =1
  AND a.unit_id         =c.unit_id
  AND c.status          =1
  ),
  list_result AS
  (SELECT a.groupUnitCode,
    a.groupUnitId,
    a.unitId,
    a.unitName,
    a.unitCode,
    a.groupUnitDetailId,
    CASE
      WHEN llx.lee_value IS NULL
      THEN a.groupUnitName
      ELSE llx.lee_value
    END groupUnitName
  FROM list_search a
  LEFT JOIN list_language_exchange llx
  ON a.groupUnitId = llx.bussiness_id
  )
SELECT b.groupUnitCode,
  b.groupUnitId,
  b.unitId,
  b.unitName,
  b.unitCode,
  b.groupUnitDetailId,
  b.groupUnitName
FROM list_result b
  ) re
WHERE 1=1
