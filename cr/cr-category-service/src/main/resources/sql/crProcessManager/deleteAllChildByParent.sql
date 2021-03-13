-- delete from open_pm.CR_FILES_ATTACH where cr_id= :cr_id and temp_import_id is not null
delete from cr_process
 where cr_process_id = :cr_process_id
 or cr_process_id in (     select a.cr_process_id     from cr_process a     start with a.parent_id = :cr_process_id     connect by prior a.cr_process_id = a.parent_id )

