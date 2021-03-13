SELECT
  a.cr_id crId,
  a.state state,
  b.file_id fileId,
  a.created_date createdDate,
  b.file_type fileType,
  a.EARLIEST_START_TIME earliestStartTime
FROM
  open_pm.cr a
INNER JOIN open_pm.cr_files_attach b
ON
  a.cr_id = b.cr_id
WHERE
  a.cr_number  = :crNumber
AND b.dt_code IS NOT NULL
