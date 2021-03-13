select  us.USER_ID receiverId,
        us.FULLNAME receiverFullName,
        us.USERNAME receiverUsername,
        us.MOBILE receiverPhone,
        us.user_Language userLanguage
from    WO_CD_GROUP g,
        wo w,
        wo_cd c,
        COMMON_GNOC.USERS us
where   w.cd_id = g.wo_group_id
and     c.WO_GROUP_ID = g.WO_GROUP_ID
and     us.USER_ID = c.USER_ID
and     w.wo_id = :woId
