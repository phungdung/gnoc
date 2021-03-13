SELECT  r.ID id,
        r.RISK_CHANGE_STATUS_ID riskChangeStatusId,
        r.ROLE_ID roleId,
        ci.ITEM_NAME roleName
FROM    RISK_CHANGE_STATUS_ROLE r
        LEFT JOIN (select * from COMMON_GNOC.CAT_ITEM
                    where STATUS = 1
                    and PARENT_ITEM_ID is null
                    and CATEGORY_ID = (select CATEGORY_ID
                                        from COMMON_GNOC.CATEGORY
                                        where CATEGORY_CODE = :categoryCode
                                        and status = 1)) ci
            ON TO_CHAR(r.ROLE_ID) = ci.ITEM_VALUE
WHERE   r.RISK_CHANGE_STATUS_ID = :riskChangeStatusId
ORDER BY r.ID ASC
