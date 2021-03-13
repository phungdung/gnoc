SELECT wo_id woId,
  status,
  ft_id ftId,
  wo_code woCode,
  wo_system_id woSystemId,
  WO_TYPE_ID woTypeId
FROM wfm.wo
WHERE wo_system_id = :wo_system_id
AND wo_system      = 'CR'
-- AND create_date    >
--   (SELECT cr.created_date FROM open_pm.cr WHERE cr_id = :cr_id
--   )
AND NOT EXISTS
  (SELECT a.wo_name
  FROM open_pm.cr_process_wo a
  WHERE a.IS_REQUIRE_WO = 0
  AND a.cr_process_id  IN (:cr_process_id)
  AND LOWER(WO_CONTENT) LIKE '%'
    || LOWER(a.wo_name)
    ||'%'
  )
