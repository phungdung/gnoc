SELECT SERVICE_CODE serviceCode,
  listagg(ROLE_CODE, ',') within GROUP (
ORDER BY SERVICE_ID) roleCode,
listagg(EXECUTION_UNIT, ',') within GROUP (
ORDER BY SERVICE_ID) executionUnit,
listagg(SERVICE_ID, ',') within GROUP (
ORDER BY SERVICE_ID) serviceIdStr
FROM OPEN_PM.SR_CATALOG
WHERE 1 = 1
