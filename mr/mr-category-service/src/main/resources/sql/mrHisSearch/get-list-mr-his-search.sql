SELECT a.mhs_id mhsId,
  a.user_id userId,
  us.username userName,
  a.unit_id unitId,
  ut.unit_name unitName,
  ut.unit_code unitCode,
  a.status status,
  TO_CHAR(a.change_date, 'dd/MM/yyyy HH24:mi:ss') changeDate,
  a.comments comments,
  a.mr_id mrId,
  a.notes notes,
  a.action_code actionCode
FROM mr_his a
LEFT JOIN common_gnoc.unit ut
ON a.unit_id = ut.unit_id
LEFT JOIN common_gnoc.users us
ON us.user_id = a.user_id
WHERE 1       = 1
