SELECT  a.risk_his_id riskHisId,
        a.old_status oldStatus,
        cat1.item_name oldStatusName,
        a.new_status newStatus,
        cat2.item_name newStatusName,
        a.risk_id riskId,
        a.content content,
        a.user_id userId,
        a.user_name userName,
        a.update_time + :offset * interval '1' hour updateTime,
        a.is_send_message isSendMesssage
FROM    wfm.risk_history a
        LEFT JOIN (select * from COMMON_GNOC.CAT_ITEM
                    where STATUS = 1
                    and PARENT_ITEM_ID is null
                    and CATEGORY_ID = (select CATEGORY_ID from COMMON_GNOC.CATEGORY
                                        where CATEGORY_CODE = :categoryCode
                                        and status = 1)) cat1
                    ON a.old_status = cat1.item_value
        LEFT JOIN (select * from COMMON_GNOC.CAT_ITEM
                    where STATUS = 1
                    and PARENT_ITEM_ID is null
                    and CATEGORY_ID = (select CATEGORY_ID from COMMON_GNOC.CATEGORY
                                        where CATEGORY_CODE = :categoryCode
                                        and status = 1)) cat2
                    ON a.new_status = cat2.item_value
WHERE   a.risk_id = :riskId
ORDER BY a.update_time DESC
