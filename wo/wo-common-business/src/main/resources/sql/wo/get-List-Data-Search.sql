SELECT w.wo_id woId,
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
         WHEN 0
                 THEN :unassigned
         WHEN 1
                 THEN :assigned
         WHEN 2
                 THEN :reject
         WHEN 3
                 THEN :dispatch
         WHEN 4
                 THEN :accept
         WHEN 5
                 THEN :inProcess
         WHEN 9
                 THEN :pending
         WHEN 8
                 THEN :closedCd
         WHEN 6
                 THEN (
           CASE
             WHEN w.result IS NULL
                     THEN :closedFt
             ELSE :closedCd
               END)
         WHEN 7
                 THEN :draft
         ELSE ''
           END statusName,
       w.priority_id priorityId,
       wp.priority_name priorityName,
       to_char(w.start_time  + :offset * interval '1' hour,'dd/MM/yyyy HH24:mi:ss') startTime,
       to_char(w.end_time    + :offset * interval '1' hour,'dd/MM/yyyy HH24:mi:ss') endTime,
       to_char(w.finish_time + :offset * interval '1' hour,'dd/MM/yyyy HH24:mi:ss') finishTime,
       w.result result,
       w.station_id stationId,
       w.station_code stationCode,
       w.last_update_time + :offset * interval '1' hour lastUpdateTime,
       w.file_name fileName,
       w.wo_description comments,
       g.wo_group_name cdName,
       cp.username createPersonName,
       f.username ftName,
       wd.account_isdn accountIsdn,
       w.REASON_OVERDUE_LV1_ID reasonOverdueLV1Id,
       w.REASON_OVERDUE_LV1_NAME reasonOverdueLV1Name,
       w.REASON_OVERDUE_LV2_ID reasonOverdueLV2Id,
       w.REASON_OVERDUE_LV2_NAME reasonOverdueLV2Name,
       to_char(w.COMPLETED_TIME + :offset * interval '1' hour,'dd/MM/yyyy HH24:mi:ss') completedTime
FROM wo w,
     wo p,
     wo_type c,
     wo_cd_group g,
     common_gnoc.users f,
     common_gnoc.users cp,
     wo_priority wp,
     wo_detail wd
WHERE 1                = 1
  AND w.wo_type_id       = c.wo_type_id
  AND w.create_person_id = cp.user_id
  AND w.cd_id            = g.wo_group_id
  AND w.ft_id            = f.user_id(+)
  AND w.parent_id        = p.wo_id(+)
  AND w.priority_id      = wp.priority_id(+)
  AND w.wo_id            = wd.wo_id(+)
