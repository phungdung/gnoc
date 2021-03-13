SELECT DISTINCT  a.cr_process_id crProcessId, b.cpte_id cpteId,     b.file_type fileType,
c.temp_import_id tempImportId, c.code code,
c.name name, c.title title, c.is_active isActive   FROM open_pm.cr_process_template b
left join open_pm.cr_process  a on a.cr_process_id = b.cr_process_id
left join open_pm.temp_import c on c.temp_import_id = b.temp_import_id   where 1 = 1
