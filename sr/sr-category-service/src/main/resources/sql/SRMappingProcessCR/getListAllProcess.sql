WITH parent_list AS
  ( SELECT DISTINCT cr_process_id
  FROM CR_PROCESS
  WHERE parent_id IS NULL
  AND is_active    = 1
  )
SELECT a.cr_process_id crProcessId ,
  a.cr_process_code crProcessCode,
  a.cr_process_name crProcessName
FROM CR_PROCESS a
WHERE level                      = 2
  CONNECT BY prior cr_process_id = parent_id
  START WITH cr_process_id      IN
  (SELECT cr_process_id FROM parent_list
  )
