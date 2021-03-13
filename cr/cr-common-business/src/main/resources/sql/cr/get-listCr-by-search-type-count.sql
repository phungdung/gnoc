SELECT
  COUNT (cr.cr_id) crId,
  CR_TYPE crType
FROM
  open_pm.cr cr
LEFT JOIN common_gnoc.users usOri
ON
  cr.change_orginator = usOri.user_id
WHERE
  usOri.is_enable = 1
