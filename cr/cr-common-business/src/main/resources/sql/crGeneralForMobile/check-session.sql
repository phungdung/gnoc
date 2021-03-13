select a.user_id userId, a.session_id sessionId
from common_gnoc.user_mobile_session a
where
 a.insert_time>sysdate-2
 and a.session_id= :session_id
