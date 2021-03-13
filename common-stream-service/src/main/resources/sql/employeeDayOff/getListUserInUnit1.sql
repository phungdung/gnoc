WITH tmp AS
  (SELECT unit_id
  FROM
    (SELECT *
    FROM COMMON_GNOC.UNIT
    WHERE 1                    = 1
    AND (IS_COMMITTEE          = 0
    OR IS_COMMITTEE           IS NULL)
      CONNECT BY prior Unit_id = PARENT_UNIT_ID
      START WITH Unit_id       = :unitId
    UNION
    SELECT * FROM COMMON_GNOC.UNIT WHERE Unit_id = :unitId
    )
  ORDER BY IS_COMMITTEE ASC
  )
SELECT UNIT_ID unitId,
  USER_ID userId,
  USERNAME username,
  FULLNAME fullname,
  MOBILE mobile,
  EMAIL email
FROM common_gnoc.users
WHERE unit_id IN
  (SELECT * FROM tmp) AND USERS.IS_ENABLE = 1
