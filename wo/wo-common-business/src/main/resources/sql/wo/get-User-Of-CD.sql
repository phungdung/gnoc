SELECT  u.* FROM COMMON_GNOC.USERS u
WHERE   1 = 1
AND     u.USER_ID IN (SELECT  c.USER_ID
                      FROM    wfm.WO_CD c
                      WHERE   c.WO_GROUP_ID = :cdId)
