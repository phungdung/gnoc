SELECT
  a.cr_number crNumber,
  a.state state,
  TO_CHAR(a.earliest_start_time,'dd/MM/yyyy HH24:mi:ss') impactStartTime,
  TO_CHAR(a.latest_start_time,'dd/MM/yyyy HH24:mi:ss') impactEndTime,
  g.staff_code userExecuteCode,
  g.username userExecute
FROM
  open_pm.cr a
LEFT JOIN common_gnoc.users g
ON
  a.change_responsible=g.user_id
WHERE
  g.username             = :userName
AND a.earliest_start_time>to_date(:startTime,'dd/MM/yyyy HH24:mi:ss')
AND a.state              = :state
