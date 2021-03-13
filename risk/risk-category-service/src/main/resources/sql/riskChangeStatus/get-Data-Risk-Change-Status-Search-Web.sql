SELECT  c.ID id,
        c.RISK_TYPE_ID riskTypeId,
        t.RISK_TYPE_NAME riskTypeName,
        c.OLD_STATUS oldStatus,
        ci1.ITEM_NAME oldStatusName,
        c.NEW_STATUS newStatus,
        ci2.ITEM_NAME newStatusName,
        c.IS_DEFAULT isDefault,
        CASE c.IS_DEFAULT
          WHEN 1 THEN :ok
          ELSE :nok
        END isDefaultName,
        c.RISK_PRIORITY riskPriority,
        ci3.ITEM_NAME riskPriorityName
FROM    RISK_CHANGE_STATUS c
        LEFT JOIN RISK_TYPE t ON c.RISK_TYPE_ID = t.RISK_TYPE_ID
        LEFT JOIN (select * from COMMON_GNOC.CAT_ITEM
                    where STATUS = 1
                    and PARENT_ITEM_ID is null
                    and CATEGORY_ID = (select CATEGORY_ID
                                        from COMMON_GNOC.CATEGORY
                                        where CATEGORY_CODE = :categoryCode
                                        and status = 1)) ci1
            ON TO_CHAR(c.OLD_STATUS) = ci1.ITEM_VALUE
        LEFT JOIN (select * from COMMON_GNOC.CAT_ITEM
                    where STATUS = 1
                    and PARENT_ITEM_ID is null
                    and CATEGORY_ID = (select CATEGORY_ID
                                        from COMMON_GNOC.CATEGORY
                                        where CATEGORY_CODE = :categoryCode
                                        and status = 1)) ci2
            ON TO_CHAR(c.NEW_STATUS) = ci2.ITEM_VALUE
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci3 ON c.RISK_PRIORITY = ci3.ITEM_ID
WHERE   1 = 1
