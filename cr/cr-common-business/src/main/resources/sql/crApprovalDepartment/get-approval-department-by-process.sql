SELECT
  ut.unit_id unitId,
  CASE
    WHEN ut.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(ut.unit_code
      || ' ('
      ||ut.unit_name
      ||')')
  END        AS unitName,
  0          AS status,
  (ROWNUM+2) AS cadtLevel
FROM
  common_gnoc.unit ut
WHERE
  ut.unit_id =
  (
    SELECT
      other_dept
    FROM
      cr_process
    WHERE
      cr_process_id = :crProcessId
    AND rownum      <2
  )
