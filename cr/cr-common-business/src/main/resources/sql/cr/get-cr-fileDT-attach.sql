WITH lst_file AS
  (SELECT a.FILE_ID fileId,
    b.cr_number crNumber,
    b.cr_id crId,
    a.file_name fileName,
    c.username username,
    a.file_path filePath
  FROM open_pm.cr_files_attach a,
    open_pm.cr b,
    common_gnoc.users c
  WHERE a.cr_id              =b.cr_id
  AND a.user_id              =c.user_id
  AND a.file_type           IN('3')
  AND b.cr_number            = :crNumber
  AND a.time_attack         >= to_date(:attachTime,'dd/MM/yyyy HH24:mi:ss')
  AND b.earliest_start_time >= to_date(:attachTime,'dd/MM/yyyy HH24:mi:ss')
  ORDER BY a.file_type DESC
  )
SELECT cr_file.crNumber crNumber,
  gn_file.FILE_NAME fileName,
  cr_file.username username,
  gn_file.PATH filePath
FROM lst_file cr_file
JOIN COMMON_GNOC.GNOC_FILE gn_file
ON cr_file.crId          = gn_file.BUSINESS_ID
AND cr_file.fileId       = gn_file.MAPPING_ID
AND gn_file.BUSINESS_CODE='CR_FILES_ATTACH'
