SELECT
  COUNT(cr_id) crId,
  cr.cr_type crType
FROM
  open_pm.cr cr
LEFT JOIN common_gnoc.users usOri
ON
  cr.change_orginator = usOri.user_id
WHERE
  usOri.is_enable = 1

