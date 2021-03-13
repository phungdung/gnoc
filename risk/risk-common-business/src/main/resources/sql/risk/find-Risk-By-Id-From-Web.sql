WITH lang_exchange_risk_type AS (
  SELECT  t.RISK_TYPE_ID,
          t.RISK_TYPE_CODE,
          t.RISK_TYPE_NAME,
          le2.LEE_VALUE,
          le2.LEE_LOCALE
  FROM    RISK_TYPE t
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.RISK_TYPE_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = :systemRisk
                                                      AND le2.APPLIED_BUSSINESS = :bussinessRisk
                                                      AND le2.LEE_LOCALE = :leeLocale)
select  r.risk_id riskId,
        r.risk_code riskCode,
        r.risk_name riskName,
        r.effect effect,
        r.effect_detail effectDetail,
        r.system_id systemId,
        r.is_external_vtnet isExternalVtnet,
        r.result_processing resultProcessing,
        r.frequency_detail frequencyDetail,
        r.frequency frequency,
        r.reason_accept reasonAccept,
        r.reason_reject reasonReject,
        r.reason_cancel reasonCancel,
        r.suggest_solution suggestSolution,
        r.closed_date closedDate,
        r.opened_date openedDate,
        r.accepted_date acceptedDate,
        r.canceled_date canceledDate,
        r.received_date receivedDate,
        r.rejected_date rejectedDate,
        r.log_time logTime,
        sys.system_name systemName,
        r.subject_id subjectId,
        r.solution solution,
        r.redundancy redundancy,
        (select c.ITEM_NAME from COMMON_GNOC.CAT_ITEM c where STATUS = 1 and PARENT_ITEM_ID is null
        and CATEGORY_ID = (select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE='RISK_REDUNDANCY')
        and c.item_id = r.redundancy ) redundacyName,
        r.evedence evedence,
        r.result result,
        r.risk_comment riskComment,
        unc.unit_name createUnitName,
        unc.unit_id createUnitId,
        r.create_time + :offset * interval '1' hour createTime,
        r.description description,
        r.last_update_time + :offset * interval '1' hour lastUpdateTime,
        r.risk_type_id riskTypeId,
        lert.risk_type_name riskTypeName,
        r.priority_id priorityId,
        r.start_time + :offset * interval '1' hour startTime,
        r.end_time + :offset * interval '1' hour endTime,
        r.receive_user_id receiveUserId,
        usr.username receiveUserName,
        r.receive_unit_id receiveUnitId,
        unr.unit_name receiveUnitName,
        unr.unit_code receiveUnitCode,
        r.other_system_code otherSystemCode,
        r.create_user_Id createUserId,
        r.insert_source insertSource,
        r.status status,
        case when r.status = :statusClose then round((cast(r.end_time as date) - cast(r.close_time as date)) * 24,2)
             else round((cast(r.end_time as date) - sysdate) * 24,2)
          end remainTime,
        usc.username createUserName,
        r.close_time + :offset * interval '1' hour closeTime,
        r.change_reason changeReason,
        r.IS_SEND_SMS_OVERDUE isSendSmsOverdue
from    risk r,
        lang_exchange_risk_type lert,
--         risk_type t,
        risk_system sys,
        common_gnoc.users usr,
        common_gnoc.unit unr,
        common_gnoc.users usc,
        common_gnoc.unit unc
where   1 = 1
and     r.risk_type_id = lert.risk_type_id(+)
and     r.system_id = sys.id(+)
and     r.receive_user_id = usr.user_id(+)
and     r.create_user_Id = usc.user_id(+)
and     r.receive_unit_id = unr.unit_id(+)
and     r.create_unit_id = unc.unit_id(+)
