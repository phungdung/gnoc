SELECT
  T1.ROLE_USER_ID roleUserId,
  T1.USER_NAME username,
  T1.ROLE_CODE roleCode,
  T1.STATUS status,
  T1.CREATED_USER createdUser,
  T1.CREATED_TIME createdTime,
  T1.UPDATED_USER updatedUser,
  T1.UPDATED_TIME updatedTime,
  T1.COUNTRY country,
  L.LOCATION_NAME countryName,
  T1.UNIT_ID unitId,
   CASE
    WHEN T3.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(T3.unit_code
      || ' ('
      || T3.unit_name
      || ')')
  END AS unitName,
  T1.IS_LEADER isLeader
FROM OPEN_PM.SR_ROLE_USER T1
LEFT JOIN COMMON_GNOC.UNIT T3
ON T1.UNIT_ID = T3.UNIT_ID
LEFT JOIN COMMON_GNOC.CAT_LOCATION L
ON T1.COUNTRY = L.LOCATION_ID
WHERE 1 = 1
