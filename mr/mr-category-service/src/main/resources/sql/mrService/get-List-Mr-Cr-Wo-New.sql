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
  tmp_cr AS
  (SELECT cr.notes ,
    cr.cr_id,
    cr.cr_number,
    cr.change_responsible ,
    cr.change_responsible_unit ,
    cr.consider_unit_id ,
    cr.state,
    un.UNIT_NAME responsibleUnitCRName,
    un1.UNIT_NAME considerUnitCRName
  FROM open_pm.cr cr
  LEFT JOIN common_gnoc.unit un
  ON cr.change_responsible_unit = un.Unit_Id
  LEFT JOIN common_gnoc.unit un1
  ON cr.consider_unit_id = un1.Unit_Id
  WHERE 1                =1
  AND CREATED_DATE      >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss')
  AND CREATED_DATE      <= TO_DATE(:endDate,'dd/MM/yyyy HH24:mi:ss')
  ) ,
  tmp_mr_his AS
  (SELECT mr_id,
    MIN(change_date) change_date,
    status
  FROM open_pm.MR_HIS
  WHERE 1          =1
  AND STATUS       = 6
  AND CHANGE_DATE >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss')
  GROUP BY mr_id,
    status
  ) ,
  tmp_cr_created AS
  (SELECT cr_id,
    object_id
  FROM open_pm.cr_created_from_other_sys
  WHERE system_id = 1
  ) ,
  tmp_cr_his AS
  (SELECT cr_id,
    MIN(change_date) change_date,
    action_code,
    return_code,
    status
  FROM open_pm.cr_his
  WHERE 1          =1
  AND CHANGE_DATE >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss')
  AND CHANGE_DATE <= TO_DATE(:endDate,'dd/MM/yyyy HH24:mi:ss')
  GROUP BY cr_id,
    action_code,
    return_code,
    status
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
    CASE
      WHEN cr.state         = 9
      AND crHis.action_Code = 25
      AND crHis.return_Code = 43
      THEN '7'
      WHEN cr.state         = 9
      AND crHis.action_Code = 25
      AND crHis.return_Code = 44
      THEN '8'
      WHEN cr.state          = 9
      AND crHis.action_Code  = 10
      AND crHis.return_Code IN
        ( SELECT DISTINCT RCCG_ID
        FROM open_pm.return_code_catalog
        WHERE return_category = 10
        AND is_active         = 1
        )
      THEN '9'
      WHEN cr.state         = 9
      AND crHis.action_Code = 49
      THEN '10'
      WHEN cr.state         = 9
      AND crHis.action_Code = 6
      AND TO_CHAR(crHis.return_Code) IN
          (SELECT DISTINCT RCCG_ID
          FROM open_pm.return_code_catalog
          WHERE return_category = 6
          AND is_active         = 1
          )
      THEN '11'
      WHEN cr.state         = 9
      AND crHis.action_Code = 21
      AND TO_CHAR(crHis.return_Code) IN
          (SELECT DISTINCT RCCG_ID
          FROM open_pm.return_code_catalog
          WHERE return_category = 21
          AND is_active         = 1
          )
      THEN '12'
      WHEN cr.state         = 9
      AND crHis.action_Code = 37
      AND TO_CHAR(crHis.return_Code) IN
          (SELECT DISTINCT RCCG_ID
          FROM open_pm.return_code_catalog
          WHERE return_category = 37
          AND is_active         = 1
          )
      THEN '13'
      WHEN cr.state         = 9
      AND crHis.action_Code = 25
      AND crHis.return_Code = 50
      THEN '14'
      WHEN cr.state         = 9
      AND crHis.action_Code = 3
      AND TO_CHAR(crHis.return_Code) IN
          (SELECT DISTINCT RCCG_ID
          FROM open_pm.return_code_catalog
          WHERE return_category = 3
          AND is_active         = 1
          )
      THEN '15'
      ELSE TO_CHAR(a.state)
    END state,
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
    a.impact impact,
    a.is_service_affected isServiceAffected,
    a.affected_service_id affectedServiceId,
    a.node_type nodeType,
    a.wo_id woId,
    a.create_person_id createPersonId,
    a.mr_code mrCode,
    a.cycle cycle,
    CASE
      WHEN crHis.status = 9
      THEN crHis.change_date
      ELSE NULL
    END mrCloseDate,
    a.created_time createdTime,
    a.unitCreateMr unitCreateMr,
    a.unitCreateMrName unitCreateMrName,
    cr.notes note,
    cr.change_responsible_unit changeResponsibleUnit,
    cr.responsibleUnitCRName responsibleUnitCRName,
    cr.consider_unit_id considerUnitId,
    cr.considerUnitCRName considerUnitCRName,
    cr.cr_id crId,
    cr.cr_number crNumber,
    cr.change_responsible changeResponsible,
    cr.change_responsible_unit responsibleUnitCR,
    cr.consider_unit_id considerUnitCR,
    NULL AS mrContentId ,
    wo.wo_code woCode,
    --       wl.WLG_TEXT wlgText
    (
    SELECT RTRIM(XMLAGG(XMLELEMENT(E,WLG_TEXT,',').EXTRACT('//text()')).GetClobVal() ,',') WLG_TEXT
    FROM open_pm.work_log
    WHERE 1             =1
    AND WLG_OBJECT_TYPE = 2
    AND CREATED_DATE   >= TO_DATE(:startDate,'dd/MM/yyyy HH24:mi:ss')
    AND CREATED_DATE   <= TO_DATE(:endDate,'dd/MM/yyyy HH24:mi:ss')
    AND WLG_OBJECT_ID   = cr.cr_id
    GROUP BY WLG_OBJECT_TYPE,
      WLG_OBJECT_ID
    ) wlgText
  FROM tmp_mr a
  LEFT JOIN tmp_mr_his ah
  ON a.mr_id = ah.mr_id
  LEFT JOIN tmp_cr_created cro
  ON cro.object_id = a.mr_id
  LEFT JOIN tmp_cr cr
  ON cr.cr_id = cro.cr_id
  LEFT JOIN tmp_cr_his crHis
  ON cr.cr_id  = crHis.cr_id
  AND cr.state = crHis.status
    --     LEFT JOIN tmp_worklog wl
    --     ON
    --       cr.cr_id = wl.WLG_OBJECT_ID
  LEFT JOIN WFM.WO wo
  ON a.wo_id = wo.wo_id
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
    WHEN a.state = '7'
    THEN 'CLOSE_PART_COMPLETE'
    WHEN a.state = '8'
    THEN 'CLOSE_ROLLBACK'
    WHEN a.state = '9'
    THEN 'CLOSE_FAIL_APPROVE'
    WHEN a.state = '10'
    THEN 'CLOSE_MR_FAIL_ASSIGNCAB'
    WHEN a.state = '11'
    THEN 'CLOSE_MR_FAIL_QUEUE'
    WHEN a.state = '12'
    THEN 'CLOSE_MR_FAIL_SCHEDULE'
    WHEN a.state = '13'
    THEN 'CLOSE_MR_FAIL_RECEIVE'
    WHEN a.state = '14'
    THEN 'CLOSE_MR_MISSING'
    WHEN a.state = '15'
    THEN 'CLOSE_MR_APPROVED'
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
  woCode,
  crId,
  crNumber,
  mrCode,
  changeResponsible,
  cycle,
  TO_CHAR(mrCloseDate, 'dd/MM/yyyy HH24:mi:ss') mrCloseDate,
  TO_CHAR(createdTime, 'dd/MM/yyyy HH24:mi:ss') createdTime ,
  responsibleUnitCR ,
  considerUnitCR ,
  createPersonId,
  changeResponsibleUnit,
  responsibleUnitCRName,
  considerUnitCRName,
  considerUnitId,
  unitCreateMr ,
  unitCreateMrName,
  countryName,
  regionName,
  mrContentId ,
  DECODE(note,NULL,'',note
  || ';')
  || wlgText AS note
FROM raw_data a
WHERE 1=1
