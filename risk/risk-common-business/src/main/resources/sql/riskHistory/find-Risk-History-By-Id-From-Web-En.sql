SELECT  a.risk_his_id riskHisId,
        a.old_status oldStatus,
        (select lee_value from COMMON_GNOC.LANGUAGE_EXCHANGE where APPLIED_BUSSINESS = :bussiness
                and APPLIED_SYSTEM = :system and BUSSINESS_ID = a.old_status) oldStatusName,
        a.new_status newStatus,
        (select lee_value from COMMON_GNOC.LANGUAGE_EXCHANGE where APPLIED_BUSSINESS = :bussiness
                and APPLIED_SYSTEM = :system and BUSSINESS_ID = a.new_status) newStatusName,
        a.risk_id riskId,
        a.content content,
        a.user_id userId,
        a.user_name userName,
        a.update_time updateTime,
        a.is_send_message isSendMesssage,
        a.infor_mation inforMation
FROM    wfm.risk_history a
WHERE   a.risk_his_id = :riskHisId

