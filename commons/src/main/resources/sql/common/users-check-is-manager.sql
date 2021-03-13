select user_id as userId,
is_manager
from common_gnoc.v_user_role
where is_manager = 1
and user_id = :user_id
