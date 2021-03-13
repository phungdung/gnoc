SELECT
tw.CREATE_USER_NAME createUserName,
tw.CREATE_UNIT_NAME createUnitName,
tw.CREATED_TIME createdTime,
tw.WORKLOG worklog
FROM TROUBLE_WORKLOG tw
WHERE tw.TROUBLE_ID =:troubleId
