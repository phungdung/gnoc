WITH tmp_mr AS
  (SELECT *
  FROM open_pm.mr
  WHERE 1             =1
  AND mr_Type!        = 'Bảo dưỡng cứng thiết bị (physical maintenance)'
  AND (EARLIEST_TIME >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss')
  OR LASTEST_TIME    >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss') )
  AND( EARLIEST_TIME <= TO_DATE(:endDateMr,'dd/MM/yyyy HH24:mi:ss')
  OR LASTEST_TIME    <= TO_DATE(:endDateMr,'dd/MM/yyyy HH24:mi:ss') )
  ) ,
  tmp_cr AS
  (SELECT notes ,
    cr_id ,
    change_responsible ,
    change_responsible_unit ,
    consider_unit_id
  FROM open_pm.cr
  WHERE 1           =1
  AND CREATED_DATE >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss')
  AND CREATED_DATE <= TO_DATE(:endDateMr,'dd/MM/yyyy HH24:mi:ss')
  ) ,
  tmp_worklog AS
  (SELECT WLG_OBJECT_TYPE,
    WLG_OBJECT_ID,
    RTRIM(XMLAGG(XMLELEMENT(E,WLG_TEXT,',').EXTRACT('//text()')).GetClobVal(),',') WLG_TEXT
  FROM open_pm.work_log
  WHERE 1             =1
  AND WLG_OBJECT_TYPE = 2
  AND CREATED_DATE   >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss')
  AND CREATED_DATE   <= TO_DATE(:endDateMr,'dd/MM/yyyy HH24:mi:ss')
  GROUP BY WLG_OBJECT_TYPE,
    WLG_OBJECT_ID
  ) ,
  tmp_mr_his AS
  (SELECT mr_id,
    MIN(change_date) change_date,
    status
  FROM open_pm.MR_HIS
  WHERE STATUS     =6
  AND CHANGE_DATE >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss')
  GROUP BY mr_id,
    status
  ) ,
  tmp_cr_created AS
  (SELECT cr_id,
    object_id
  FROM open_pm.cr_created_from_other_sys
  WHERE system_id = 1
  ) ,
  raw_data AS
  (SELECT a.mr_id mrId,
    a.mr_technichcal mrTechnichcal,
    a.mr_title mrTitle,
    a.mr_type mrType ,
    a.subcategory subcategory,
    a.description description,
    a.mr_works mrWorks,
    a.unit_approve unitApprove,
    a.unit_execute unitExecute,
    a.assign_to_person assignToPerson,
    a.person_accept personAccept,
    a.state,
    a.earliest_time earliestTime,
    a.lastest_time lastestTime,
    a.interval interval,
    a.next_wo_create nextWoCreate,
    a.priority_code priorityCode,
    a.country country,
    a.region region,
    a.circle circle,
    a.impact impact,
    a.is_service_affected isServiceAffected,
    a.affected_service_id affectedServiceId,
    a.node_type nodeType,
    a.wo_id woId,
    a.create_person_id createPersonId,
    a.mr_code mrCode,
    a.cycle cycle,
    ah.change_date mrCloseDate,
    a.created_time createdTime,
    u.unit_id AS unitCreateMr,
    cr.notes note,
    cr.change_responsible_unit changeResponsibleUnit,
    cr.consider_unit_id considerUnitId,
    cr.cr_id crId,
    cr.change_responsible changeResponsible,
    cr.change_responsible_unit responsibleUnitCR,
    cr.consider_unit_id considerUnitCR,
    NULL AS countryName,
    NULL AS regionName,
    NULL AS mrContentId ,
    wl.WLG_TEXT wlgText
  FROM tmp_mr a
  LEFT JOIN tmp_mr_his ah
  ON a.mr_id = ah.mr_id
  LEFT JOIN tmp_cr_created cro
  ON cro.object_id = a.mr_id
  LEFT JOIN tmp_cr cr
  ON cr.cr_id = cro.cr_id
  LEFT JOIN tmp_worklog wl
  ON cr.cr_id = wl.WLG_OBJECT_ID
  LEFT JOIN common_gnoc.users u
  ON a.create_person_id = u.user_id
  )
SELECT mrId,
  mrTechnichcal,
  mrTitle,
  mrType ,
  subcategory,
  description,
  mrWorks,
  unitApprove,
  unitExecute,
  assignToPerson,
  personAccept,
  CASE
    WHEN a.state = '1'
    THEN 'OPEN'
    WHEN a.state = '2'
    THEN 'INACTIVE_WAITTING'
    WHEN a.state = '3'
    THEN 'QUEUE'
    WHEN a.state = '4'
    THEN 'ACTIVE'
    WHEN a.state = '5'
    THEN 'INACTIVE'
    ELSE 'CLOSE'
  END state,
  TO_CHAR(earliestTime, 'dd/MM/yyyy HH24:mi:ss') earliestTime,
  TO_CHAR(lastestTime, 'dd/MM/yyyy HH24:mi:ss') lastestTime,
  interval,
  nextWoCreate,
  priorityCode,
  country,
  region,
  circle,
  impact,
  isServiceAffected,
  affectedServiceId,
  nodeType,
  woId,
  crId,
  mrCode,
  changeResponsible,
  cycle,
  TO_CHAR(mrCloseDate, 'dd/MM/yyyy HH24:mi:ss') mrCloseDate,
  TO_CHAR(createdTime, 'dd/MM/yyyy HH24:mi:ss') createdTime ,
  responsibleUnitCR ,
  considerUnitCR ,
  createPersonId,
  changeResponsibleUnit,
  considerUnitId,
  unitCreateMr ,
  countryName,
  regionName,
  mrContentId ,
  DECODE(note,NULL,'',note
  || ';')
  || wlgText AS note
FROM raw_data a
WHERE 1=1
