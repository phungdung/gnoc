SELECT us.user_id   AS userId,
  us.username       AS username,
  us.FULLNAME       AS fullname,
  ut.UNIT_NAME      AS unitName,
  ut.UNIT_ID        AS unitId,
  gte.TIMEZONE_CODE AS timezoneCode,
  gte.TIMEZONE_NAME AS timezoneName,
  CASE
    WHEN gte.TIMEZONE_OFFSET IS NULL
    THEN 0
    ELSE gte.TIMEZONE_OFFSET
  END AS userTimeZone
FROM COMMON_GNOC.users us
LEFT JOIN COMMON_GNOC.unit ut
ON us.unit_id = ut.UNIT_ID
LEFT JOIN COMMON_GNOC.GNOC_TIMEZONE gte
ON ut.TIME_ZONE        = gte.GNOC_TIMEZONE_ID
WHERE ut.STATUS        = 1
AND us.IS_ENABLE       = 1
AND (lower(us.username)= :username
OR lower(us.staff_code)= :username )
