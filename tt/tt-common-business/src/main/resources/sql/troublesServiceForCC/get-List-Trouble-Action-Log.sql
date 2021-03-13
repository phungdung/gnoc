select  a.id,
        a.CREATE_TIME as createTime ,
        a.CREATE_UNIT_ID as createUnitId,
        a.CREATE_USER_ID as createUserId,
        type,
        a.CREATER_UNIT_NAME as createrUnitName,
        a.CREATER_USER_NAME as createrUserName,
        b.item_name as stateName,
        ts.TROUBLE_CODE as troubleCode,
        a.STATE_ID stateId, a.CONTENT content,
        a.ROOT_CAUSE rootCause,
        a.WORK_ARROUND workArround
from    ONE_TM.TROUBLE_ACTION_LOGS a
inner join common_gnoc.cat_item b on a.STATE_ID = b.item_id
inner join ONE_TM.TROUBLES ts on a.TROUBLE_ID = ts.TROUBLE_ID
where lower(ts.trouble_code) = :troubleCode
order by a.CREATE_TIME desc
