SELECT a.work_log_id workLogId,
  a.wlg_object_type wlgObjectType,
  a.wlg_object_id wlgObjectId,
  a.user_id userId,
  a.user_group_action userGroupAction,
  ug.ugcy_name userGroupActionName,
  a.wlg_text wlgText,
  a.wlg_effort_hours wlgEffortHours,
  a.wlg_effort_minutes wlgEffortMinutes,
  a.wlg_access_type wlgAccessType,
  a.created_date createdDate,
  a.wlay_id wlayId,
  u.username userName,
  ug.ugcy_name userGroupName
FROM work_log a
LEFT JOIN common_gnoc.users u
ON a.user_id = u.user_id
LEFT JOIN user_group_category ug
ON ug.ugcy_id = a.user_group_action
WHERE 1       = 1
