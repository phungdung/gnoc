SELECT
  b.cr_number crNumber,
  a.file_name fileName,
  c.username username,
  d.PATH filePath,
  TO_CHAR(a.time_attack,'dd/MM/yyyy HH24:mi:ss') timeAttach
FROM
  open_pm.cr_files_attach a
  JOIN open_pm.cr b ON a.cr_id =b.cr_id
  JOIN common_gnoc.users c ON a.user_id =c.user_id
  LEFT JOIN COMMON_GNOC.GNOC_FILE d ON (a.FILE_ID = d.MAPPING_ID AND d.BUSINESS_CODE = 'CR_FILES_ATTACH')
WHERE
  1 = 1
AND a.file_type            = :fileType
AND b.cr_number            = :crNumber
AND a.time_attack         >= to_date(:attachTime,'dd/MM/yyyy HH24:mi:ss')
AND b.earliest_start_time >= to_date(:attachTime,'dd/MM/yyyy HH24:mi:ss')
ORDER BY
  a.file_type DESC
