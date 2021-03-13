SELECT  w.WO_WORKLOG_ID woWorklogId,
        w.WO_ID woId,
        w.WO_WORKLOG_CONTENT woWorklogContent,
        w.WO_SYSTEM woSystem,
        w.WO_SYSTEM_ID woSystemId,
        w.USER_ID userId,
        w.USERNAME username,
        w.UPDATE_TIME + :offset * interval '1' hour updateTime,
        w.NATION nation
FROM    WO_WORKLOG w
WHERE   w.WO_ID = :woId
ORDER BY  w.UPDATE_TIME DESC
