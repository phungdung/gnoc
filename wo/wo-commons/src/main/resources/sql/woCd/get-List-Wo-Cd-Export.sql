SELECT  c.CD_ID cdId,
        c.WO_GROUP_ID woGroupId,
        g.WO_GROUP_CODE woGroupCode,
        g.WO_GROUP_NAME woGroupName,
        c.USER_ID userId,
        u.USERNAME userName,
        u.FULLNAME fullName,
        u.EMAIL email,
        u.MOBILE mobile
FROM    WO_CD c
        LEFT JOIN WO_CD_GROUP g ON c.WO_GROUP_ID = g.WO_GROUP_ID
        LEFT JOIN COMMON_GNOC.USERS u ON c.USER_ID = u.USER_ID
WHERE   1 = 1
