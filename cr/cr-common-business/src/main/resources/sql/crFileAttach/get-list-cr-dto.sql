SELECT
a.FILE_ID fileId,
d.FILE_NAME fileName,
TO_CHAR(a.time_attack,'dd/MM/yyyy') timeAttack,
a.USER_ID userId,
a.FILE_TYPE fileType,
a.CR_ID crId,
d.PATH filePath,
a.TEMP_IMPORT_ID tempImportId,
a.FILE_SIZE fileSize,
a.DT_CODE dtCode,
a.DT_FILE_HISTORY dtFileHistory
FROM
  open_pm.cr_files_attach a
  LEFT JOIN COMMON_GNOC.GNOC_FILE d ON (a.FILE_ID = d.MAPPING_ID AND d.BUSINESS_CODE = 'CR_FILES_ATTACH')
WHERE
1 = 1
