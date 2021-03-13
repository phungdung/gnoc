SELECT UNIT_ID unitId,
  USER_ID userId,
  USERNAME username,
  FULLNAME fullname,
  MOBILE mobile,
  EMAIL email
FROM common_gnoc.users
WHERE unit_id = :unitId AND USERS.IS_ENABLE = 1
