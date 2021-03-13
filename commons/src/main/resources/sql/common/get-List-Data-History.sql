select
    his.HIS_ID hisId,
    his.USER_ID userId,
    (select us.username from common_gnoc.users us where us.user_id = his.user_id ) userName,
    his.CREATE_TIME createTime,
    (select cat.ITEM_NAME from CAT_ITEM cat where cat.ITEM_CODE = his.type) type,
    his.NEW_OBJECT newObject,
    his.RESULT result,
    his.ACTION_TYPE actionType
from COMMON_GNOC.HIS_USER_IMPACT his
where 1 = 1
