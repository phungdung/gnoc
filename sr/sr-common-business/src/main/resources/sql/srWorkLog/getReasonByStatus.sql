SELECT reason.REASON_REJECT_ID reasonRejectId,
  reason.sr_status srStatus,
  reason.REASON reason ,
  type.WL_TYPE_ID wlTypeId
FROM SR_REASON_REJECT reason
INNER JOIN SR_WORKLOG_TYPE type
ON type.SR_STATUS LIKE '%'
  || reason.SR_STATUS
  || '%'
WHERE type.WL_TYPE_ID =:p_wl_type_id
