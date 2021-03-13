WITH
lang_exchange_priority AS (
  SELECT  p.PRIORITY_ID,
          p.PRIORITY_NAME,
          p.PRIORITY_CODE,
          le1.LEE_VALUE,
          le1.LEE_LOCALE
  FROM    WO_PRIORITY p
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le1 ON p.PRIORITY_ID = le1.BUSSINESS_ID
    AND le1.APPLIED_SYSTEM = 4
    AND le1.APPLIED_BUSSINESS = 3
    AND le1.LEE_LOCALE = :leeLocale
),
lang_exchange_wo_type AS (
  SELECT  t.WO_TYPE_ID,
          t.WO_TYPE_CODE,
          t.WO_TYPE_NAME,
          t.TIME_OVER,
          le2.LEE_VALUE,
          le2.LEE_LOCALE
  FROM    WO_TYPE t
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.WO_TYPE_ID = le2.BUSSINESS_ID
    AND le2.APPLIED_SYSTEM = 4
    AND le2.APPLIED_BUSSINESS = 1
    AND le2.LEE_LOCALE = :leeLocale
),
list_work_logs AS (
  select wo_id, count(wo_worklog_id) count_work_log
  from WO_WORKLOG
  GROUP BY wo_id
)
SELECT
w.wo_id woId,
  w.parent_id parentId,
  (SELECT p.wo_code FROM wo p WHERE p.WO_ID = w.PARENT_ID) parentName,
--  p.wo_code parentName,
  w.wo_code woCode,
  w.wo_content woContent,
  w.wo_system woSystem,
  w.wo_system_id woSystemId,
  w.wo_system_out_id woSystemOutId,
  w.create_person_id createPersonId,
  w.create_date + :offset * interval '1' hour createDate,
  w.wo_type_id woTypeId,
  case
      when
      lewt.LEE_VALUE is null
      then to_char(lewt.WO_TYPE_NAME)
      else to_char(lewt.LEE_VALUE) end woTypeName,
  lewt.time_over timeOver,
  w.cd_id cdId,
  w.ft_id ftId,
  w.status status,
  CASE w.status
    WHEN 0 THEN :unassigned
    WHEN 1 THEN :assigned
    WHEN 2 THEN (case when w.ft_id is not null then :reject
                      else :rejectCd end)
    WHEN 3 THEN :dispatch
    WHEN 4 THEN :accept
    WHEN 5 THEN :inProcess
    WHEN 9 THEN :pending
    WHEN 8 THEN :closedCd
    WHEN 6 THEN (case when w.result is null then :closedFt
                      else :closedCd end)
    WHEN 7 THEN :draft
    ELSE '' END statusName,
  w.priority_id priorityId,
  case
      when
      lep.LEE_VALUE is null
      then to_char(lep.PRIORITY_NAME)
      else to_char(lep.LEE_VALUE) end priorityName,
  w.start_time + :offset * interval '1' hour startTime,
  w.end_time + :offset * interval '1' hour endTime,
  w.finish_time + :offset * interval '1' hour finishTime,
  w.result result,
  w.station_id stationId,
  w.station_code stationCode,
  w.last_update_time + :offset * interval '1' hour lastUpdateTime,
  w.file_name fileName,
  w.wo_description comments,
  g.wo_group_name cdName,
  cp.username createPersonName,
  f.username ftName,
  f.fullname ftFullName,
  f.mobile ftMobile,
  wd.account_isdn accountIsdn,
  w.need_support needSupport,
  w.REASON_OVERDUE_LV1_ID reasonOverdueLV1Id,
  w.REASON_OVERDUE_LV1_NAME reasonOverdueLV1Name,
  w.REASON_OVERDUE_LV2_ID reasonOverdueLV2Id,
  w.REASON_OVERDUE_LV2_NAME reasonOverdueLV2Name,
  w.COMPLETED_TIME + :offset * interval '1' hour completedTime,
  w.COMMENT_COMPLETE commentComplete,
  (case when w.status in (7,2) then null
        else
          case when w.status = 8 then round((cast(w.end_time as date)-cast(w.finish_time as date))*24,2)
              else round((cast(w.end_time as date)-sysdate)*24,2)
          end
        end) remainTime,
  w.line_Code lineCode,
  w.is_Completed_On_Vsmart isCompletedOnVsmart,
  w.is_Call isCall,
  w.WAREHOUSE_CODE warehouseCode,
  w.CONSTRUCTION_CODE constructionCode,
  w.cd_assign_id cdAssignId,
  wti.kedb_code kedbCode,
  wti.kedb_id kedbId,
  wki.contract_Id contractId,
  wki.process_Action_Id processActionId,
  wti.able_mop ableMop,
  w.delta_Close_Wo deltaCloseWo,
  w.confirm_Not_Create_Alarm confirmNotCreateAlarm,
  (uc.unit_name||'('||uc.unit_code||')') createUnitName,
  w.PLAN_CODE planCode,
  w.NUM_SUPPORT numSupport,
  w.ft_accepted_time + :offset * interval '1' hour ftAcceptedTime,
  wd.cc_Service_Id ccServiceId,
  wd.infra_Type infraType,
  wd.service_id serviceId,
  w.wo_description woDescription,
  w.CABLE_CODE cableCode,
  w.CABLE_TYPE_CODE cableTypeCode,
  w.DISTANCE distance,
  w.VIBA_LINE_CODE vibaLineCode,
  w.LONGITUDE longitude,
  w.LATITUDE latitude,
  w.wo_worklog_content woWorklogContent,
  w.reason_detail reasonDetail,
  wki.ACTIVE_ENVIRONMENT_ID activeEnvironmentId,
  lwl.count_work_log hasWorklog,
  w.REASON_EXTENTION reasonExtention,
  w.UNIT_APPROVE_EXTEND unitApproveExtend
FROM wo w
--LEFT JOIN wo p on w.parent_id = p.wo_id
INNER JOIN wo_cd_group g on w.cd_id = g.wo_group_id
INNER JOIN lang_exchange_wo_type lewt on w.wo_type_id = lewt.wo_type_id
LEFT JOIN lang_exchange_priority lep on w.priority_id = lep.priority_id
LEFT JOIN wo_detail wd on w.wo_id = wd.wo_id
LEFT JOIN wo_trouble_info wti on w.wo_id = wti.wo_id
LEFT JOIN wo_ktts_info wki on w.wo_id = wki.wo_id
LEFT JOIN common_gnoc.users f on w.ft_id = f.USER_ID
INNER JOIN common_gnoc.users cp on w.create_person_id = cp.USER_ID
INNER JOIN common_gnoc.unit uc on cp.UNIT_ID = uc.UNIT_ID
LEFT JOIN list_work_logs lwl on w.wo_id = lwl.wo_id
WHERE 1=1
