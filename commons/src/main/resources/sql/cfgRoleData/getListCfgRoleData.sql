SELECT rd.ID id,
  rd.USERNAME username,
  rd.UNIT_ID unitId,
  rd.SYSTEM system,
  rd.ROLE role,
  rd.STATUS status,
  rd.AUDIT_UNIT_ID auditUnitId
FROM COMMON_GNOC.CFG_ROLE_DATA rd
WHERE 1 = 1
