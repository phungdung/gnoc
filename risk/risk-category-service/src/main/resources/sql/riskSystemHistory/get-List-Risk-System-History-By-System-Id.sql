SELECT  h.RISK_SYSTEM_HISTORY_ID riskSystemHistoryId,
        h.SYSTEM_ID systemId,
        h.USER_ID userId,
        us.USERNAME userName,
        h.CONTENT content,
        h.UPDATE_TIME + :offset * interval '1' hour updateTime
FROM    RISK_SYSTEM_HISTORY h
        LEFT JOIN COMMON_GNOC.USERS us ON h.USER_ID = us.USER_ID
WHERE   h.SYSTEM_ID = :systemId
ORDER BY h.UPDATE_TIME DESC
