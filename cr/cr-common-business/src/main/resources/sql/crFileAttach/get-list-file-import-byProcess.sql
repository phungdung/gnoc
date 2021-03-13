	SELECT a.file_id fileId, 
	a.file_name fileName, 
	TO_CHAR(a.time_attack, 'dd/MM/yyyy HH24:mi:ss') timeAttack, 
	a.user_id userId, 
	a.file_type fileType, 
	a.cr_id crId, 
	gf.PATH filePath,
	a.temp_import_id tempImportId,
	u.username userName
	FROM cr_files_attach a
	LEFT JOIN common_gnoc.users u on a.user_id = u.user_id
  LEFT JOIN common_gnoc.gnoc_file gf ON a.file_id = gf.mapping_id
	where a.file_type in (:fileTypeIn, :fileTypeOut)
  AND gf.BUSINESS_CODE = 'CR_FILES_ATTACH'
