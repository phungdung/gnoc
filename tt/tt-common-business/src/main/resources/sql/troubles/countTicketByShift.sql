select  nvl((select count(*)
              from one_tm.troubles aa
              where aa.CREATED_TIME > to_date(:startTime, 'dd/MM/yyyy HH24:mi:ss')
              and aa.CREATED_TIME < to_date(:endTime, 'dd/MM/yyyy HH24:mi:ss')
              and aa.RECEIVE_UNIT_ID = :receiveUnitId
              and aa.priority_id = a.priority_id
              group by aa.priority_id), 0) troubleId, /* ticket phat sing trong ca */
        nvl((select count(*)
              from one_tm.troubles aa
              where aa.state in (9,10,11)
              and aa.CLEAR_TIME > to_date(:startTime, 'dd/MM/yyyy HH24:mi:ss')
              and aa.CLEAR_TIME < to_date(:endTime, 'dd/MM/yyyy HH24:mi:ss')
              and CREATED_TIME > sysdate - 30
              and aa.RECEIVE_UNIT_ID = :receiveUnitId
              and aa.priority_id = a.priority_id
              group by aa.priority_id), 0) troubleCode, /* ticket da xu ly trong ca */
        nvl((select count(*)
              from (select  trouble_id,
                            priority_id,
                            case
                              when state in(3,5,6,8,9,11,10)
                                then nvl(time_process, 0) - (nvl(time_used, 0) + round((nvl(clear_time, sysdate) - nvl(assign_time_temp, assign_time)) * 24, 2))
                              when state = 7
                                then nvl(time_process, 0) - nvl(time_used, 0)
                              else null
                            end remainTime
                    from one_tm.troubles
                    where state in (9,10,11)
                    and CLEAR_TIME > to_date(:startTime, 'dd/MM/yyyy HH24:mi:ss')
                    and CLEAR_TIME < to_date(:endTime, 'dd/MM/yyyy HH24:mi:ss')
                    and CREATED_TIME > sysdate - 30
                    and RECEIVE_UNIT_ID = :receiveUnitId) aa
              where aa.remainTime + 8 < 0
              and aa.priority_id = a.priority_id
              group by aa.priority_id), 0) troubleName,  /* ticket qua han ca truoc dc xl trong ca nay */
        nvl((select count(*)
              from (select  trouble_id,
                            priority_id,
                            case
                              when state in(3,5,8,9,11,10,6)
                                then nvl(time_process, 0) - (nvl(time_used, 0) + round((nvl(clear_time, sysdate) - nvl(assign_time_temp, assign_time)) * 24, 2))
                              when state = 7
                                then nvl(time_process, 0) - nvl(time_used, 0)
                              else null
                            end remainTime
                    from one_tm.troubles
                    where CREATED_TIME > sysdate - 30
                    and CREATED_TIME < to_date(:endTime, 'dd/MM/yyyy HH24:mi:ss')
                    and RECEIVE_UNIT_ID = :receiveUnitId) aa
              where aa.remainTime < 0
              and aa.remainTime + 8 >= 0
              and aa.priority_id = a.priority_id
              group by aa.priority_id), 0) description, /* ticket qua han phat sinh moi trong ca */
        nvl((select count(*)
              from (select  trouble_id,
                            priority_id,
                            case
                              when state in(3,5,8,9,11,10,6)
                                then nvl(time_process, 0) - (nvl(time_used, 0) + round((nvl(clear_time, sysdate) - nvl(assign_time_temp, assign_time)) * 24, 2))
                              when state = 7
                                then nvl(time_process, 0) - nvl(time_used, 0)
                              else null
                            end remainTime
                    from one_tm.troubles
                    where state in(9,10,11)
                    and CLEAR_TIME > to_date(:startTime, 'dd/MM/yyyy HH24:mi:ss')
                    and CLEAR_TIME < to_date(:endTime, 'dd/MM/yyyy HH24:mi:ss')
                    and CREATED_TIME > sysdate - 30
                    and RECEIVE_UNIT_ID = :receiveUnitId) aa
              where aa.remainTime > 0
              and aa.priority_id = a.priority_id
              group by aa.priority_id), 0) typeId, /* ticket dung han xu ly trong ca */
        nvl((select count(*)
              from (select  trouble_id,
                            priority_id,
                            case
                              when state in(1,3,4,5,6,7,8)
                                then nvl(time_process, 0) - (nvl(time_used, 0) + round((nvl(clear_time, sysdate) - nvl(assign_time_temp, assign_time)) * 24, 2))
                              when state = 7
                                then nvl(time_process, 0) - nvl(time_used, 0)
                              else null
                            end remainTime
                    from one_tm.troubles
                    where state in(1,3,4,5,6,7,8)
                    and CREATED_TIME > sysdate - 30
                    and RECEIVE_UNIT_ID = :receiveUnitId) aa
              where aa.remainTime < 0
              and aa.priority_id = a.priority_id
              group by aa.priority_id), 0) subCategoryId, /* ticket ton qua han trong ca */
        a.priority_id priorityId
from    one_tm.troubles a
where   a.priority_id in (61, 1000, 1952)
and     a.CREATED_TIME > sysdate - 45
and     a.RECEIVE_UNIT_ID = :receiveUnitId
group by a.priority_id
order by a.priority_id desc

