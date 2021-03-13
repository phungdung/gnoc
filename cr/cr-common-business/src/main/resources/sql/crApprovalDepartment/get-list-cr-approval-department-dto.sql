SELECT
  cadt.cadt_id cadtId,
  cadt.unit_id unitId,
  cadt.user_id userId,
  TO_CHAR(cadt.approved_date, 'dd/MM/yyyy HH24:mi:ss') approvedDate,
  cadt.notes notes,
  cadt.cadt_level cadtLevel,
  cadt.status status,
  CASE
    WHEN ut.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(ut.unit_code
      || ' ('
      ||ut.unit_name
      ||')')
  END AS unitName,
  CASE
    WHEN us.username IS NULL
    THEN ''
    ELSE TO_CHAR(us.username
      || ' ('
      ||us.fullname
      ||')')
  END AS userName
FROM
  open_pm.cr_approval_department cadt
LEFT JOIN common_gnoc.unit ut
ON
  cadt.unit_id = ut.unit_id
LEFT JOIN common_gnoc.users us
ON
  cadt.user_id = us.user_id
WHERE
1=1

