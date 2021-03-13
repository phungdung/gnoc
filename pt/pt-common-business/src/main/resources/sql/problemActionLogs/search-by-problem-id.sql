 SELECT PROBLEM_ACTION_LOGS_ID problemActionLogsId,
  content,
  CREATE_TIME  + :offset * interval '1' hour createTime,
   CREATE_UNIT_ID createUnitId,
 CREATER_UNIT_NAME createrUnitName,
 CREATE_USER_ID createUserId,
  TYPE,
  PROBLEM_ID problemId,
  CREATER_USER_NAME createrUserName,
  NOTE note
 FROM PROBLEM_ACTION_LOGS
 WHERE 1 = 1
