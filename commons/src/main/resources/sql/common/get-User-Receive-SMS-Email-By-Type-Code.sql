SELECT  u.USER_ID userId,
        u.SMS_TYPE smsType,
        u.TYPE_CODE typeCode,
        u.LAST_UPDATE_TIME lastUpdateTime,
        u.MOBILE mobile,
        u.EMAIL email
FROM    USER_SMS u
WHERE   1 = 1
