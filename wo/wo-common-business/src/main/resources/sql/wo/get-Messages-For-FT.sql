select  us.USER_ID receiverId,
        us.FULLNAME receiverFullName,
        us.USERNAME receiverUsername,
        us.MOBILE receiverPhone,
        us.user_Language userLanguage
from    wo w left join common_gnoc.users us on w.FT_ID = us.USER_ID
where   w.wo_id = :woId
