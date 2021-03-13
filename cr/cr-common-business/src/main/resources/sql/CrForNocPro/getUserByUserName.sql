SELECT TO_CHAR(a.USER_ID) userId ,
  a.USERNAME username ,
  TO_CHAR(a.UNIT_ID) unitId ,
  a.STAFF_CODE staffCode ,
  a.EMAIL email ,
  a.MOBILE mobile
FROM COMMON_GNOC.USERS a
WHERE a.IS_ENABLE     = 1
AND UPPER(a.USERNAME) = :username
