SELECT  a.trouble_id troubleId,
        a.priority_id priorityId,
        p.item_name priorityName,
        p.item_code priorityCode,
        a.trouble_name troubleName,
        a.trouble_code troubleCode,
        a.state state,
        s.item_name stateName,
        a.type_id typeId,
        mang.item_name typeName,
        a.created_time createdTime,
        case when s.item_code in('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED')
            then nvl(a.time_process,0)- (nvl(a.time_used,0) + round( (nvl(a.clear_time,sysdate)-nvl(a.assign_time_temp,a.assign_time))*24,2))
            when s.item_code in ('DEFERRED')
            then nvl(a.time_process,0)- nvl(a.time_used,0)
            else null end remainTime,
        a.clear_time clearTime,
        a.last_update_time lastUpdateTime,
        a.receive_unit_id receiveUnitId,
        a.receive_user_id receiveUserId,
        a.receive_unit_name receiveUnitName,
        a.receive_user_name receiveUserName
FROM    one_tm.troubles a,
        common_gnoc.cat_item p,
        common_gnoc.cat_item s,
        common_gnoc.cat_item mang
WHERE   1 = 1
AND     a.priority_id = p.item_id
AND     a.state = s.item_id
AND     a.type_id = mang.item_id
-- AND     not exists (select null from ONE_TM.PROBLEMS b where a.TROUBLE_CODE = b.RELATED_TT and RELATED_TT is not null)
