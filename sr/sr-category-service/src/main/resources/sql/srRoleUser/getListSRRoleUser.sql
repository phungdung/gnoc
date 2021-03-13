SELECT T1.ROLE_USER_ID roleUserId,
  T1.USER_NAME username,
  T1.ROLE_CODE roleCode,
  T2.ROLE_NAME roleName,
  T1.STATUS status,
  T1.CREATED_USER createdUser,
  T1.CREATED_TIME createdTime,
  T1.UPDATED_USER updatedUser,
  T1.UPDATED_TIME updatedTime,
  T1.COUNTRY country,
  T1.UNIT_ID unitId,
  T1.IS_LEADER isLeader,
  T2.PARENT_CODE parentCode,
  T2.GROUP_ROLE groupRole,
  CASE
    WHEN T3.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(T3.unit_code
      || ' ('
      || T3.unit_name
      || ')')
  END AS unitName
FROM OPEN_PM.SR_ROLE_USER T1
INNER JOIN OPEN_PM.SR_ROLE T2
ON T1.ROLE_CODE = T2.ROLE_CODE
LEFT JOIN COMMON_GNOC.UNIT T3
ON T1.UNIT_ID = T3.UNIT_ID
WHERE 1       = 1
