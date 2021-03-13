SELECT
  a.trouble_id troubleId,
  a.trouble_name troubleName,
  a.trouble_code troubleCode,
  a.state,
  s.item_name stateName,
  a.receive_unit_id receiveUnitId,
  a.receive_user_id receiveUserId,
  a.receive_unit_name receiveUnitName,
  a.receive_user_name receiveUserName,
  case when s.item_code in('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED')
       then nvl(a.time_used,0) + round( (nvl(a.clear_time,sysdate)-nvl(a.assign_time_temp,a.assign_time))*24 ,2)
       else nvl(a.time_used,0) end timeUsed,
  case when s.item_code in('WAITING RECEIVE','QUEUE','SOLUTION FOUND','CLEAR','CLOSED','CLOSED NOT KEDB','WAIT FOR DEFERRED')
       then nvl(a.time_process,0)- (nvl(a.time_used,0) + round( (nvl(a.clear_time,sysdate)-nvl(a.assign_time_temp,a.assign_time))*24,2))
       when s.item_code in ('DEFERRED')
       then nvl(a.time_process,0)- nvl(a.time_used,0)
       else null end remainTime,
  a.description, a.info_Ticket infoTicket,
  a.root_cause rootCause,
  a.work_arround workArround,
  a.solution_type solutionType,
  a.work_log workLog,
  to_char(a.begin_trouble_time,'dd/MM/yyyy HH24:MI:ss') beginTroubleTime,
  to_char(a.end_trouble_time,'dd/MM/yyyy HH24:MI:ss') endTroubleTime,
  a.create_user_name createUserName,
  a.create_unit_name createUnitName
FROM
  one_tm.troubles a,
  common_gnoc.cat_item s
WHERE
  a.state = s.item_id
