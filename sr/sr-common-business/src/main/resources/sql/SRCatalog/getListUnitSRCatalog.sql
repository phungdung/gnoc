SELECT t1.SERVICE_ID serviceId,
  t1.execution_unit unitId,
  TO_CHAR(ut.unit_code
  || ' ('
  || ut.unit_name
  || ')') unitName,
  t1.country country ,
  t1.AUTO_CREATE_SR autoCreateSR,
  t1.ROLE_CODE roleCode
FROM open_pm.sr_catalog t1
JOIN common_gnoc.unit ut
ON t1.execution_unit = ut.unit_id
WHERE 1              = 1
