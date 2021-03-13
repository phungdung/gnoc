SELECT cr.earliest_start_time earliestStartTime,
       cr.latest_start_time latestStartTime,
       cr.cr_number crNumber,
       cr.title title,
       vdip.ip nodeIp,
       vdip.device_code nodeCode,
       cr.cr_id crId
FROM cr_impacted_nodes cins
       LEFT JOIN cr
         ON cr.cr_id           = cins.cr_id
              AND cins.insert_time >= TRUNC(cr.created_date)
       LEFT JOIN
         (SELECT iip.ip_id ipId,
                 iip.ip_id ,
                 iip.ip ip,
                 ide.device_id ,
                 ide.device_code ,
                 TO_CHAR(ide.device_name) device_Name,
                 ide.device_code_old ,
                 ide.network_type
          FROM common_gnoc.infra_ip iip
                 LEFT JOIN common_gnoc.infra_device ide
                   ON ide.device_id            = iip.device_id
         ) vdip ON vdip.ip_Id        = cins.ip_id
WHERE cins.device_id         IS NOT NULL
  AND cr.earliest_start_time   IS NOT NULL
  AND cr.latest_start_time     IS NOT NULL
  AND ((cr.earliest_start_time <= :startDate
          AND cr.latest_Start_Time     >= :startDate)
         OR (cr.earliest_start_time   <= :endDate
               AND cr.latest_Start_Time     >= :endDate))
  AND vdip.device_code          IS NOT NULL
  AND vdip.ip                   IS NOT NULL
  AND vdip.ip_id               IN (:ipx)
  AND cr.cr_id                 <> :crId
  AND ((cr.state               IN (:stateAccept,:stateResolve))
         OR (cr.state                  = :stateClose
               AND cr.cr_return_code_id     IS NOT NULL))
ORDER BY cr.earliest_start_time, vdip.ip
