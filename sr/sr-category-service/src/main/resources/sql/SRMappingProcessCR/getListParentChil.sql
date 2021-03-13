WITH parent_list AS
  ( SELECT DISTINCT cr_process_id
  FROM CR_PROCESS
  WHERE parent_id IS NULL
  AND is_active    = 1
  ),
  raw_data AS
  (SELECT level lv,
    a.cr_process_id crProcessId ,
    a.cr_process_code crProcessCode,
    parent_id,
    a.cr_process_name
  FROM CR_PROCESS a
  WHERE level                      = 2
  OR level                         = 3
    CONNECT BY prior cr_process_id = parent_id
    START WITH cr_process_id      IN
    (SELECT cr_process_id FROM parent_list
    )
  ),
  main_data AS
  (SELECT d.crProcessCode crProcessCode,
    d.cr_process_name wo,
    CASE
      WHEN d.lv = 2
      THEN NULL
      ELSE cr.cr_process_code
    END crProcessParentCode ,
    CASE
      WHEN d.lv = 2
      THEN NULL
      ELSE cr.cr_process_name
    END process
  FROM raw_data d
  LEFT JOIN cr_process cr
  ON d.parent_id = cr.cr_process_id
  )
SELECT crProcessParentCode,
  process,
  crProcessCode,
  wo
FROM main_data
ORDER BY crProcessParentCode

