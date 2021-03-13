select
  w.wo_id woId,
  w.parent_id parentId,
  p.wo_code parentName,
  w.wo_code woCode,
  w.wo_content woContent,
  w.wo_system woSystem,
  w.wo_system_id woSystemId,
  w.wo_system_out_id woSystemOutId,
  w.create_person_id createPersonId,
  w.create_date + :offset * interval '1' hour createDate,
  w.wo_type_id woTypeId,
  c.wo_type_name woTypeName,
  w.cd_id cdId,
  w.ft_id ftId,
  w.status status,
  CASE w.status
    WHEN 0 THEN :unassigned
    WHEN 1 THEN :assigned
    WHEN 2 THEN :reject
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
  wp.priority_name priorityName,
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
  wd.account_isdn accountIsdn
from
  wo w,
  wo p,
  wo_type c,
  wo_cd_group g,
  common_gnoc.users f,
  common_gnoc.users cp,
  wo_priority wp,
  wo_detail wd
where 1 = 1
and w.wo_type_id = c.wo_type_id
and w.create_person_id = cp.user_id
and w.cd_id = g.wo_group_id
and w.ft_id = f.user_id(+)
and w.parent_id = p.wo_id(+)
and w.priority_id = wp.priority_id(+)
and w.wo_id = wd.wo_id(+)
