SELECT
  a.cr_number crNumber,
  a.state state,
  c.staff_code createUserCode,
  c.username createUser,
  TO_CHAR(a.earliest_start_time,'dd/MM/yyyy HH24:mi:ss') impactStartTime,
  TO_CHAR(a.latest_start_time,'dd/MM/yyyy HH24:mi:ss') impactEndTime,
  d.staff_code userConsiderCode,
  d.username userConsider,
  g.staff_code userExecuteCode,
  g.username userExecute
FROM
  open_pm.cr a
LEFT JOIN common_gnoc.users c
ON
  a.change_orginator=c.user_id
LEFT JOIN common_gnoc.users d
ON
  a.consider_user_id=d.user_id
LEFT JOIN common_gnoc.users g
ON
  a.change_responsible=g.user_id
WHERE
  a.cr_number =:crNumber
