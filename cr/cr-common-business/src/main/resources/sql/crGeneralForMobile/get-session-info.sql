SELECT a.user_id userId,
  a.session_id sessionId,
  a.unit_id unitId
FROM common_gnoc.user_mobile_session a
WHERE 1 = 1
AND a.insert_time> sysdate-2
AND a.session_id   =:sessionId
