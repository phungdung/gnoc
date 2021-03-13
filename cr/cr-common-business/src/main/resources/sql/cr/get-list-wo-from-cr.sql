SELECT
  wo_id woId,
  status,
  ft_id ftId,
  wo_code woCode,
  wo_system_id woSystemId
FROM
  wfm.wo
WHERE
  wo_system_id  = :crId
AND wo_system   = 'CR'
AND create_date >
  (
    SELECT
      cr.created_date
    FROM
      open_pm.cr
    WHERE
      cr_id = :crId
  )
AND wo_content NOT IN
  (
    SELECT
      a.wo_name woName
    FROM
      open_pm.cr_process_wo a
    WHERE
      a.IS_REQUIRE_WO  = 0
    AND a.cr_process_id=:crProcessId
  )
