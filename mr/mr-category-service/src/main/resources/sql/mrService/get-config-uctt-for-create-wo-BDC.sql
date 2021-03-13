WITH tmp_mr AS
  (SELECT *
  FROM OPEN_PM.MR a
  WHERE 1               =1
  AND a.mr_Type         = 'Bảo dưỡng cứng thiết bị (physical maintenance)'
  AND (a.earliest_Time >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss')
  OR a.lastest_Time    >= TO_DATE(:startDateMr,'dd/MM/yyyy HH24:mi:ss') )
  AND (a.earliest_Time <= TO_DATE(:endDateMr,'dd/MM/yyyy HH24:mi:ss')
  OR a.lastest_Time    <= TO_DATE(:endDateMr,'dd/MM/yyyy HH24:mi:ss') )
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
  tmp_wo AS
  (SELECT WO_ID,
    ft_id,
    WO_CODE,
    CD_ID,
    wo_system_id,
    create_date,
    wo_system
  FROM WFM.WO
  WHERE 1       =1
  AND WO_SYSTEM = 'MR'
  AND wo_system_id LIKE 'MR_%'
  AND wo_code LIKE 'WO_MR_%'
  AND CREATE_DATE >= TO_DATE(:dateCreateWoFrom,'dd/MM/yyyy HH24:mi:ss')
  AND CREATE_DATE <= TO_DATE(:dateCreateWoTo,'dd/MM/yyyy HH24:mi:ss')
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
    NULL AS countryName ,
    NULL AS regionName ,
    wo.CD_ID cdGroupWo,
    wo.create_date creatDate,
    u.unit_id unitCreateMr
  FROM tmp_mr a
  LEFT JOIN tmp_mr_his ah
  ON a.mr_id = ah.mr_id
  LEFT JOIN tmp_wo wo
  ON a.mr_code = wo.wo_system_id
  LEFT JOIN common_gnoc.users u
  ON a.create_person_id = u.user_id
  ),
  main_data AS
  (SELECT mrId,
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
    nodeType ,
    woId,
    crId,
    mrCode,
    ftId,
    createPersonId,
    TO_CHAR(mrCloseDate, 'dd/MM/yyyy HH24:mi:ss') mrCloseDate ,
    cdGroupWo,
    countryName,
    regionName,
    unitCreateMr ,
    NULL AS note ,
    NULL AS mrContentId ,
    NULL AS cdGroupWoName
  FROM raw_data a
  )
SELECT * FROM main_data a WHERE 1=1
