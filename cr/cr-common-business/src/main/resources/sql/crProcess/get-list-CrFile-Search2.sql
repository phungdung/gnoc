SELECT a.file_id fileId,
  gf.FILE_NAME fileName,
  --   TO_CHAR(a.time_attack, 'dd/MM/yyyy HH24:mi:ss') timeAttack,
  a.time_attack timeAttack,
  a.user_id userId,
  a.file_type fileType,
  a.cr_id crId,
  gf.path filePath,
  CASE
    WHEN a.dt_file_history IS NOT NULL
    THEN 'system'
    ELSE u.username
  END userName ,
  a.TEMP_IMPORT_ID tempImportId,
  A.DT_CODE dtCode
FROM common_gnoc.gnoc_file gf
LEFT JOIN open_pm.cr_files_attach a
ON (a.file_id = gf.mapping_id)
LEFT JOIN common_gnoc.users u
ON a.user_id         = u.user_id
WHERE 1              = 1
AND gf.BUSINESS_CODE = 'CR_FILES_ATTACH'
