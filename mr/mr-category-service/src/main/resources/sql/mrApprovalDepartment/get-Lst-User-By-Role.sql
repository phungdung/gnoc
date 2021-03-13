SELECT us.user_id userId,
  us.username userName,
  ut.unit_code unitCode,
  ut.unit_id unitId,
  ut.unit_name unitName,
  rs.role_code roleCode,
  rs.role_name roleName
FROM common_gnoc.users us
LEFT JOIN common_gnoc.unit ut
ON us.unit_id = ut.unit_id
LEFT JOIN common_gnoc.role_user rur
ON rur.user_id = us.user_id
LEFT JOIN common_gnoc.roles rs
ON rs.role_id = rur.role_id
WHERE 1       =1
