SELECT a.work_log_id worklogid,
  a.wlg_object_type wlgobjecttype,
  a.wlg_object_id wlgobjectid,
  a.user_id userid,
  a.user_group_action usergroupaction,
  ug.ugcy_name usergroupactionname,
  a.wlg_text wlgtext,
  a.wlg_effort_hours wlgefforthours,
  a.wlg_effort_minutes wlgeffortminutes,
  a.wlg_access_type wlgaccesstype,
  a.created_date createddate,
  a.wlay_id wlayid,
  u.username username,
  ug.ugcy_name usergroupname
FROM open_pm.work_log a
LEFT JOIN common_gnoc.users u
ON a.user_id = u.user_id
LEFT JOIN
  (SELECT NVL (le.lee_value, ugc.ugcy_name) ugcy_name,
    ugcy_id
  FROM open_pm.user_group_category ugc
  LEFT JOIN
    (SELECT *
    FROM COMMON_GNOC.language_exchange l
    WHERE l.applied_system  = 2
    AND l.applied_bussiness = 16
    ) le
  ON ugc.ugcy_id     = le.bussiness_id
  ) ug ON ug.ugcy_id = a.user_group_action
WHERE 1              = 1

