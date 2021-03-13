SELECT a.madt_id madtId,
  a.mr_id mrId,
  a.unit_id unitId,
  ut.unit_name unitName,
  (ut.unit_name
  || ' ('
  || ut.unit_code
  ||') ') unitName,
  ut.unit_code unitCode,
  a.user_id userId,
  us.username userName,
  a.madt_level madtLevel,
  a.status status,
  TO_CHAR(a.incomming_date, 'dd/MM/yyyy HH24:mi:ss') incommingDate,
  TO_CHAR(a.approved_date, 'dd/MM/yyyy HH24:mi:ss') approvedDate,
  a.incomming_date incommingDate,
  a.approved_date approvedDate,
  a.notes notes,
  a.return_code returnCode
FROM mr_approval_department a
LEFT JOIN common_gnoc.unit ut
ON a.unit_id = ut.unit_id
LEFT JOIN common_gnoc.users us
ON us.user_id = a.user_id
WHERE 1       =1
