SELECT
  rccg.rccg_id returnCodeId,
  rccg.return_title returnTitle,
  CASE
    WHEN ut.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(ut.unit_code
      || ' ('
      ||ut.unit_name
      ||')')
  END AS unitName,
  CASE
    WHEN us.username IS NULL
    THEN ''
    ELSE TO_CHAR(us.username
      || ' ('
      ||us.fullname
      ||')')
  END AS userName,
  TO_CHAR((chs.change_date + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss')
  changeDate,
  chs.comments comments,
  chs.action_code actionCode,
  chs.status status ,
  actionCode.CR_ACTION_CODE_TITTLE AS reasonTitle
FROM
  cr_his chs
LEFT JOIN common_gnoc.unit ut
ON
  chs.unit_id = ut.unit_id
LEFT JOIN common_gnoc.users us
ON
  chs.user_id = us.user_id
LEFT JOIN open_pm.return_code_catalog rccg
ON
  rccg.rccg_id = chs.return_code
LEFT JOIN open_pm.cr cr
ON
  cr.CR_ID = chs.CR_ID
LEFT JOIN open_pm.CR_ACTION_CODE actionCode
ON
  actionCode.CR_ACTION_CODE_ID =cr.CR_RETURN_RESOLVE
AND chs.STATUS                 = 7
WHERE
  chs.cr_id = :crId
ORDER BY
  TO_CHAR(chs.change_date, 'yyyy/MM/dd HH24:mi') DESC,
  chs.action_code DESC
