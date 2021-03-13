SELECT  a.ID id,
        a.USER_NAME userName,
        a.USER_ID userId,
        a.UNIT_NAME unitName,
        a.UNIT_ID unitId,
        a.SHIFT_ID shiftId,
        a.LAST_UPDATE_TIME lastUpdateTime,
        a.STATUS status,
        a.CREATED_TIME createdTime,
        listagg(c.USERNAME,',') within group (order by null) receiveUserName
FROM    SHIFT_HANDOVER a
        LEFT JOIN SHIFT_STAFT b ON a.ID = b.SHIFT_HANDOVER_ID
        LEFT JOIN USERS c ON b.RECEIVE_USER_ID = c.USER_ID
WHERE   1 = 1
