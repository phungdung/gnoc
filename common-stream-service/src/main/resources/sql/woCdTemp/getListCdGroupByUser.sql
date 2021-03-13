SELECT  g.WO_GROUP_ID woGroupId,
        g.WO_GROUP_CODE woGroupCode,
        g.WO_GROUP_NAME woGroupName,
        g.EMAIL email,
        g.MOBILE mobile,
        g.GROUP_TYPE_ID groupTypeId,
        g.IS_ENABLE isEnable
FROM    WFM.WO_CD_GROUP g
WHERE   g.IS_ENABLE = 1
AND     1 = 1
