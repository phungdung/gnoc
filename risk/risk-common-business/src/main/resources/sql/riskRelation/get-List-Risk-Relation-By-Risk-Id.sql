SELECT  r.ID id,
        r.RISK_ID riskId,
        r.SYSTEM system,
        r.SYSTEM_CODE systemCode,
        r.SYSTEM_ID systemId,
        r.CREATE_TIME + :offset * interval '1' hour createTime,
        r.END_TIME + :offset * interval '1' hour endTime,
        r.CONTENT content,
        r.CREATE_PERSON_ID createPersonId,
        r.RECEIVE_UNIT_ID receiveUnitId
FROM    RISK_RELATION r
WHERE   r.RISK_ID = :riskId
ORDER BY r.CREATE_TIME DESC
