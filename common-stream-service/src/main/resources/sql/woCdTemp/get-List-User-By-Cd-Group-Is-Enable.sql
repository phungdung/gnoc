SELECT  u.CD_GROUP_UNIT_ID cdGroupUnitId,
        u.CD_GROUP_ID cdGroupId,
        u.UNIT_ID unitId,
        u.IS_ROOT isRoot
FROM    WFM.WO_CD_GROUP_UNIT u,
        WFM.WO_CD_GROUP g
WHERE   1 = 1
AND     u.CD_GROUP_ID = g.WO_GROUP_ID
AND     g.IS_ENABLE = 1
