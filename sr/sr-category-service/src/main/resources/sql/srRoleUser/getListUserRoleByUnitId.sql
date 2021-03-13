SELECT DISTINCT unit.UNIT_ID unitId ,
  unit.unit_code
  ||'('
  ||unit.unit_name
  ||')' unitName ,
  role.role_code roleCode
FROM common_gnoc.unit unit
LEFT JOIN OPEN_PM.sr_role_user role
ON unit.unit_id    = role.unit_id
WHERE
1=1
