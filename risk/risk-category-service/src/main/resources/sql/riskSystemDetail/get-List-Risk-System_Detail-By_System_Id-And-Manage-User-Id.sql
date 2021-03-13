SELECT  d.ID id,
        d.SYSTEM_ID systemId,
        d.MANAGE_USER_ID manageUserId,
        us.FULLNAME manageUserFullName,
        us.USERNAME manageUserName,
        d.MANAGE_UNIT_ID manageUnitId,
        un.UNIT_NAME manageUnitName,
        un.UNIT_CODE manageUnitCode
FROM    RISK_SYSTEM_DETAIL d
        LEFT JOIN COMMON_GNOC.USERS us ON d.MANAGE_USER_ID = us.USER_ID
        LEFT JOIN COMMON_GNOC.UNIT un ON d.MANAGE_UNIT_ID = un.UNIT_ID
WHERE   d.SYSTEM_ID = :systemId AND d.MANAGE_USER_ID = :manageUserId
ORDER BY us.USERNAME ASC
