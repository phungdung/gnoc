SELECT b1.CHILD_ID childId ,
  b1.SERVICE_ID serviceId ,
  b1.SERVICE_CODE serviceCode,
  b1.SERVICE_ID_CHILD serviceIdChild ,
  b1.SERVICE_CODE_CHILD serviceCodeChild,
  b1.AUTO_CREATE_SR autoCreateSR,
  b2.SERVICE_NAME serviceNameChild,
  b2.EXECUTION_UNIT executionUnitChild,
  b3.EXECUTION_UNIT executionUnitParent,
  unit.unitName executionUnitDesc,
  b1.GENERATE_NO generateNo
FROM OPEN_PM.SR_CATALOG_CHILD b1
JOIN OPEN_PM.SR_CATALOG b2
ON b1.SERVICE_ID_CHILD    = b2.SERVICE_ID
AND b1.SERVICE_CODE_CHILD = b2.SERVICE_CODE
JOIN OPEN_PM.SR_CATALOG b3
ON b1.SERVICE_ID = b3.SERVICE_ID
LEFT JOIN
  (SELECT ut.unit_id unitId,
    ut.parent_unit_id,
    ut.unit_code unitCode,
    ut.LOCATION_ID locationId,
    CASE
      WHEN ut.unit_code IS NULL
      THEN ''
      ELSE TO_CHAR(ut.unit_code
        || ' ('
        || ut.unit_name
        || ')')
    END AS unitName,
    CASE
      WHEN parentUt.unit_code IS NULL
      THEN ''
      ELSE TO_CHAR(parentUt.unit_code
        || ' ('
        || parentUt.unit_name
        || ')')
    END AS parentUnitName
  FROM common_gnoc.unit ut
  LEFT JOIN common_gnoc.unit parentUt
  ON ut.parent_unit_id        = parentUt.unit_id
  WHERE ut.status             = 1
  ) unit ON b2.EXECUTION_UNIT = unit.unitId
WHERE 1          =1
