SELECT COUNT(*) totalRow
FROM OPEN_PM.SR T1
LEFT JOIN OPEN_PM.sr_catalog T2
ON TO_NUMBER(T1.service_id)                       = T2.SERVICE_ID
WHERE 1                                           =1
AND T1.parent_code                               IS NULL
AND T1.CREATED_USER                               =:createUser
AND ( (T1.STATUS                                  =:status
AND (T1.updated_time                             + NVL(T2.TIME_CLOSED_SR,0) <= sysdate ))
OR (T1.STATUS NOT                                IN ('New','Cancelled','Rejected','Concluded','Closed')
AND IS_FORCE_CLOSED                               = 1))
