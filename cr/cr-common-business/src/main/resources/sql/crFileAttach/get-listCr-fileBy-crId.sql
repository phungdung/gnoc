SELECT
  a.file_id fileId,
  a.file_name fileName,
  a.file_type fileType,
  a.file_size fileSize,
  a.cr_id crId,
  a.temp_import_id
FROM
  cr_files_attach a
