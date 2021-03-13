select
prob.PROBLEM_WORKLOG_ID problemWorklogId,
prob.CREATE_USER_ID createUserId,
prob.CREATE_USER_NAME createUserName,
prob.CREATE_UNIT_ID createUnitId,
prob.CREATE_UNIT_NAME createUnitName,
prob.WORKLOG worklog,
prob.DESCRIPTION description,
prob.CREATED_TIME + :offset * interval '1' hour createdTime,
prob.PROBLEM_ID problemId
from PROBLEM_WORKLOG prob
where PROBLEM_ID = :problemId
