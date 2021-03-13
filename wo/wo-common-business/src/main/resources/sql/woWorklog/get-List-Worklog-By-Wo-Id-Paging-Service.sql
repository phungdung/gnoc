SELECT  w.WO_WORKLOG_ID woWorklogId,
        w.WO_ID woId,
        w.WO_WORKLOG_CONTENT woWorklogContent,
        w.WO_SYSTEM woSystem,
        w.WO_SYSTEM_ID woSystemId,
        w.USER_ID userId,
        w.USERNAME username,
        w.UPDATE_TIME updateTime,
        w.NATION nation
FROM    WO_WORKLOG w
WHERE   1=1
