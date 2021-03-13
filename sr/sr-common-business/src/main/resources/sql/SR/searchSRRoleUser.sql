SELECT ROLE_USER_ID roleUserId,
  ROLE_CODE roleCode,
  USER_NAME username,
  STATUS status,
  CREATED_USER createdUser,
  CREATED_TIME createdTime,
  UPDATED_USER updatedUser,
  UPDATED_TIME updatedTime,
  COUNTRY country,
  UNIT_ID unitId,
  IS_LEADER isLeader
FROM OPEN_PM.SR_ROLE_USER
WHERE 1=1
