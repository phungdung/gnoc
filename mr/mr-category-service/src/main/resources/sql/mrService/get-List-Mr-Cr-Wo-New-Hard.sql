WITH tmp_mr_user AS
  (SELECT mr.* ,
    a2.LOCATION_NAME countryName,
    a3.LOCATION_NAME regionName,
    u.unit_id AS unitCreateMr
  FROM open_pm.mr mr
  LEFT JOIN common_gnoc.users u
  ON mr.create_person_id = u.user_id
  LEFT JOIN COMMON_GNOC.cat_location a2
  ON mr.COUNTRY = a2.LOCATION_ID
  LEFT JOIN COMMON_GNOC.cat_location a3
  ON mr.REGION         = a3.LOCATION_ID
  WHERE 1              =1
  AND mr_Type!         = 'Bảo dưỡng cứng thiết bị (physical maintenance)'
  AND ( EARLIEST_TIME >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss')
  OR LASTEST_TIME     >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss') )
  AND ( EARLIEST_TIME <= TO_DATE(:endDate,'dd/MM/yyyy HH24:mi:ss')
  OR LASTEST_TIME     <= TO_DATE(:endDate,'dd/MM/yyyy HH24:mi:ss') )
  ) ,
  tmp_mr AS
  (SELECT mr.* ,
    un2.UNIT_NAME AS unitCreateMrName
  FROM tmp_mr_user mr
  LEFT JOIN common_gnoc.unit un2
  ON mr.unitCreateMr = un2.Unit_Id
  ) ,
  tmp_mr_his AS
  (SELECT mr_id,
    MIN(change_date) change_date,
    status
  FROM open_pm.MR_HIS
  WHERE STATUS     =6
  AND CHANGE_DATE >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss')
  GROUP BY mr_id,
    status
  ) ,
  tmp_worklog AS
  (SELECT WO_ID woId,
    RTRIM(XMLAGG(XMLELEMENT(E,WO_WORKLOG_CONTENT,',').EXTRACT('//text()')).GetClobVal(),',') note
  FROM WFM.WO_WORKLOG
  WHERE 1                 =1
  AND WO_SYSTEM           = 'MR'
  AND WO_WORKLOG_CONTENT IS NOT NULL
  AND UPDATE_TIME        >= TO_DATE(:dateCreateWoFrom,'dd/MM/yyyy HH24:mi:ss')
  AND UPDATE_TIME        <= TO_DATE(:dateCreateWoTo,'dd/MM/yyyy HH24:mi:ss')
  GROUP BY WO_ID
  ) ,
  tmp_wo AS
  (SELECT wo.WO_ID,
    wo.ft_id,
    wo.WO_CODE,
    wo.CD_ID,
    wo.wo_system_id,
    wo.create_date,
    wo.wo_system,
    g.WO_GROUP_NAME,
    wl.note note
  FROM WFM.WO wo
  LEFT JOIN wfm.WO_CD_GROUP g
  ON wo.CD_ID   = g.WO_GROUP_ID
  LEFT JOIN tmp_worklog wl
  ON wo.WO_ID   = wl.woId
  WHERE 1       =1
  AND wo.WO_SYSTEM = 'MR'
  AND wo.wo_system_id LIKE 'MR_%'
  AND wo.wo_code LIKE 'WO_MR_%'
  AND wo.CREATE_DATE >= TO_DATE(:dateCreateWoFrom,'dd/MM/yyyy HH24:mi:ss')
  AND wo.CREATE_DATE <= TO_DATE(:dateCreateWoTo,'dd/MM/yyyy HH24:mi:ss')
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
    a.countryName countryName,
    a.region region,
    a.regionName regionName,
    a.circle circle,
    a.cycle cycle,
    a.impact impact,
    a.is_service_affected isServiceAffected,
    a.affected_service_id affectedServiceId,
    a.node_type nodeType,
    wo.wo_id woId,
    a.cr_id crId,
    a.mr_code mrCode,
    wo.ft_id ftId,
    a.create_person_id createPersonId,
    ah.change_date mrCloseDate,
    a.created_time createdTime ,
    a.unitCreateMr unitCreateMr,
    a.unitCreateMrName unitCreateMrName,
    wo.CD_ID cdGroupWo,
    wo.WO_GROUP_NAME cdGroupWoName,
    wo.create_date woCreateDate,
    wo.wo_code woCode,
    wo.note note
  FROM tmp_mr a
  LEFT JOIN tmp_mr_his ah
  ON a.mr_id = ah.mr_id
  LEFT JOIN tmp_wo wo
  ON a.mr_code = wo.wo_system_id
  )
SELECT mrId,
  mrTechnichcal,
  mrTitle,
  mrType,
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
  END state ,
  TO_CHAR(earliestTime, 'dd/MM/yyyy HH24:mi:ss') earliestTime ,
  TO_CHAR(lastestTime, 'dd/MM/yyyy HH24:mi:ss') lastestTime ,
  TO_CHAR(createdTime, 'dd/MM/yyyy HH24:mi:ss') createdTime ,
  interval,
  nextWoCreate,
  priorityCode,
  country,
  region,
  circle,
  cycle,
  impact,
  isServiceAffected,
  affectedServiceId,
  nodeType,
  woId,
  crId,
  mrCode,
  ftId,
  createPersonId,
  TO_CHAR(mrCloseDate, 'dd/MM/yyyy HH24:mi:ss') mrCloseDate ,
  cdGroupWo,
  cdGroupWoName,
  countryName,
  regionName,
  unitCreateMr,
  unitCreateMrName,
  NULL AS mrContentId,
  TO_CHAR(woCreateDate, 'dd/MM/yyyy HH24:mi:ss') woCreateDate,
  woCode,
  note
FROM raw_data a
WHERE 1=1
