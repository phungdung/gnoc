select a.cr_process_id crProcessId, a.cr_process_code crProcessCode,      a.cr_process_name crProcessName,
 a.parent_id parentId  ,regexp_replace(SYS_CONNECT_BY_PATH (a.cr_process_code,' > '),' > ','',1,1)  AS description
 from cr_process a  start with a.parent_id = :parent_id
