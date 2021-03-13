select
  o.od_id odId,
  o.od_code odCode,
  o.od_name odName,
  o.create_time + :offset * interval '1' hour createTime,
  o.description ,
  o.last_update_time + :offset * interval '1' hour lastUpdateTime,
  o.od_type_id odTypeId,
  t.od_type_name odTypeName,
  o.priority_id priorityId,
  o.start_time + :offset * interval '1' hour startTime,
  o.end_time + :offset * interval '1' hour endTime,
  o.receive_user_id receiveUserId,
  usr.username receiveUserName,
  o.receive_unit_id receiveUnitId,
  unr.unit_name receiveUnitName,
  unr.unit_code receiveUnitCode,
  o.other_system_code otherSystemCode,
  o.plan_code planCode,
  o.create_Person_Id createPersonId,
  o.insert_source insertSource,
  o.status,
  (case when o.status = 15
       then round((cast(o.end_time as date)-cast(o.close_time as date))*24,2)
       else round((cast(o.end_time as date)-sysdate)*24,2)
  end) remainTime,
  o.clear_code_id clearCodeId,
  o.close_code_id closeCodeId,
  usc.username createPersonName,
  o.close_time + :offset * interval '1' hour closeTime ,
  o.end_pending_time  + :offset * interval '1' hour endPendingTime,
  o.VOFFICE_TRANS_CODE vofficeTransCode,
  unc.unit_name createUnitName,
  cit.ITEM_NAME priorityName,
  cits.ITEM_NAME statusName,
  TO_CHAR(o.FINISHED_TIME,'dd/MM/yyyy HH24:mi:ss') finishedTime
from
  wfm.od o,
  wfm.od_type t,
  common_gnoc.users usr,
  common_gnoc.unit unr,
  common_gnoc.users usc,
  common_gnoc.unit unc,
  common_gnoc.CAT_ITEM cit,
  common_gnoc.CAT_ITEM cits
where
  1 = 1
  and  o.PRIORITY_ID = cit.ITEM_ID (+)
  and  to_char(o.STATUS) = cits.ITEM_VALUE (+)
  and o.od_type_id = t.od_type_id(+)
  and o.receive_user_id = usr.user_id(+)
  and o.create_Person_Id = usc.user_id(+)
  and o.receive_unit_id = unr.unit_id(+)
  and o.create_unit_id = unc.unit_id(+)
  and cits.CATEGORY_ID = (Select cat.CATEGORY_ID from COMMON_GNOC.CATEGORY cat
  where cat.CATEGORY_CODE='OD_STATUS' and cits.STATUS = 1)

