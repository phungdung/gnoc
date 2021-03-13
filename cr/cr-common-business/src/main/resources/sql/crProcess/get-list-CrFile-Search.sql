SELECT
  a.file_id fileId,
  a.file_name fileName,
--   TO_CHAR(a.time_attack, 'dd/MM/yyyy HH24:mi:ss') timeAttack,
  a.time_attack timeAttack,
  a.user_id userId,
  a.file_type fileType,
  a.cr_id crId,
  a.file_path filePath,
  CASE
    WHEN a.dt_file_history IS NOT NULL
    THEN 'tdtt'
    ELSE u.username
  END userName
FROM
  cr_files_attach a
LEFT JOIN common_gnoc.users u
ON
  a.user_id = u.user_id
WHERE
  a.file_type   <> :processIn
AND a.file_type <> :processOut
