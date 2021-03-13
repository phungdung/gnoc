SELECT us.FULLNAME AS fullName,
  us.USERNAME AS userName,
  us.USER_ID AS userID,
  us.MOBILE AS mobile
FROM common_gnoc.users us
WHERE USERNAME NOT IN
  (SELECT username FROM MR_USER_CFG_APPROVED_SMS_BTS)
