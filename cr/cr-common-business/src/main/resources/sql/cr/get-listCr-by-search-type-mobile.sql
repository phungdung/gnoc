SELECT cr.cr_id crId,
  cr.title title,
  cr.cr_number crNumber,
  cr.DESCRIPTION description,
  cr.NOTES notes,
  cace.cr_action_code_tittle crActionCodeTittle,
  caceResolve.cr_action_code_tittle crReturnResolveTitle,
  cr.cr_type crType,
  cr.RISK risk,
  cr.COUNTRY country,
  cr.REGION region,
  cr.CIRCLE circle,
  cr.manage_unit_id manageUnitId,
  cr.manage_user_id manageUserId,
  cr.SERVICE_AFFECTING serviceAffecting,
  cr.DUTY_TYPE dutyType,
  cr.is_primary_cr isPrimaryCr,
  cr.relate_to_primary_cr relateToPrimaryCr,
  cr.relate_to_pre_approved_cr relateToPreApprovedCr,
  crPri.cr_number relateToPrimaryCrNumber,
  crPre.cr_number relateToPreApprovedCrNumber,
  (cr.earliest_start_time + :offset * interval '1' hour) earliestStartTime,
  (cr.latest_start_time + :offset * interval '1' hour) latestStartTime,
  (cr.DISTURBANCE_START_TIME + :offset * interval '1' hour) disturbanceStartTime,
  (cr.DISTURBANCE_END_TIME + :offset * interval '1' hour) disturbanceEndTime,
  (cr.created_date + :offset * interval '1' hour) createdDate,
  (cr.update_time + :offset * interval '1' hour) updateTime,

  cr.priority priority,
  CASE
    WHEN utOri.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(utOri.unit_code
      || ' ('
      ||utOri.unit_name
      ||')')
  END AS changeOrginatorUnitName,
  CASE
    WHEN usOri.username IS NULL
    THEN ''
    ELSE TO_CHAR(usOri.username
      || ' ('
      ||usOri.fullname
      ||')')
  END AS changeOrginatorName,
  CASE
    WHEN utResp.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(utResp.unit_code
      || ' ('
      ||utResp.unit_name
      ||')')
  END AS changeResponsibleUnitName,
  CASE
    WHEN usResp.username IS NULL
    THEN ''
    ELSE TO_CHAR(usResp.username
      || ' ('
      ||usResp.fullname
      ||')')
  END AS changeResponsibleName,
  CASE
    WHEN utConsi.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(utConsi.unit_code
      || ' ('
      ||utConsi.unit_name
      ||')')
  END AS considerUnitName,
  CASE
    WHEN usConsi.username IS NULL
    THEN ''
    ELSE TO_CHAR(usConsi.username
      || ' ('
      ||usConsi.fullname
      ||')')
  END AS considerUserName,
  cr.state,
  cr.cr_type_cat crTypeCat,
  cr.user_cab userCab,
  cr.change_responsible      AS changeResponsible,
  cr.change_responsible_unit AS changeResponsibleUnit,
  cr.change_orginator        AS changeOrginator,
  cr.change_orginator_unit   AS changeOrginatorUnit,
  cr.consider_unit_id        AS considerUnitId,
  cr.consider_user_id        AS considerUserId,
  CASE cr.state
    WHEN :state_close
    THEN
      CASE
        WHEN EXISTS
          (
            SELECT
              1
            FROM
              open_pm.cr_his chs1
            WHERE
              cr.cr_id            = chs1.cr_id
            AND chs1.action_code IN (:action_reject,:act_close,
              :act_close_by_appr,:act_close_by_man,:act_close_by_emer)
          )
        THEN ''
        ELSE TO_CHAR((
          (
            SELECT
              MAX(chs2.change_date)
            FROM
              open_pm.cr_his chs2
            WHERE
              cr.cr_id            = chs2.cr_id
            AND chs2.change_date >= cr.created_date
            AND chs2.action_code IN (:act_close_cr,:act_close_cr_appr,
              :act_close_excu)
          )
          + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss')
      END
    WHEN :state_draft
    THEN ''
    ELSE TO_CHAR(sysdate + :offset * interval '1' hour, 'dd/MM/yyyy HH24:mi:ss'
      )
  END AS compareDate,
  TO_CHAR(cr.AUTO_EXECUTE) AS autoExecute ,
  cr.CIRCLE_ADDITIONAL_INFO AS circleAdditionalInfo ,
  cr.WAITING_MOP_STATUS waitingMopStatus ,
  cr.VMSA_VALIDATE_KEY vMSAValidateKey ,
  cr.TOTAL_AFFECTED_CUSTOMERS totalAffectedCustomers ,
  cr.TOTAL_AFFECTED_MINUTES totalAffectedMinutes ,
  cr.LIST_WO_ID       AS listWoId ,
  cr.IS_TRACING_CR    AS isTracingCr ,
  cr.NODE_SAVING_MODE AS nodeSavingMode,
  cr.handover_ca handoverCa,
  cr.is_handover_ca isHandoverCa ,
  cr.is_load_mop isLoadMop,
  cr.IS_CONFIRM_ACTION isConfirmAction,
  cr.RANK_GATE rankGate,
  cr.IS_RUN_TYPE isRunType ,
  cr.PROCESS_TYPE_LV3_ID processTypeLv3Id ,
  cr.respone_time responeTime,
  cr.IMPACT_SEGMENT impactSegmentId,
  ist.impact_segment_name impactSegment,
  cr.CHILD_IMPACT_SEGMENT childImpactSegmentId,
  ccay.CHILDREN_NAME childImpactSegment,
  cr.DEVICE_TYPE deviceTypeId,
  REPLACE(dts.device_type_name, '/',', ') deviceType,
  sy.subcategory_id subcategoryId,
  sy.sy_name subcategory,
  cr.PROCESS_TYPE_ID processTypeId,
  cr.process_type_id AS crProcessId,
  :CR_PROCESS_NAME,
  ass.SERVICE_CODE affectedService
FROM open_pm.cr cr
LEFT JOIN common_gnoc.unit utOri
ON cr.change_orginator_unit = utOri.unit_id
LEFT JOIN common_gnoc.users usOri
ON cr.change_orginator = usOri.user_id
LEFT JOIN common_gnoc.unit utResp
ON cr.change_responsible_unit = utResp.unit_id
LEFT JOIN common_gnoc.users usResp
ON cr.change_responsible = usResp.user_id
LEFT JOIN common_gnoc.unit utConsi
ON cr.consider_unit_id = utConsi.unit_id
LEFT JOIN common_gnoc.users usConsi
ON cr.consider_user_id = usConsi.user_id
LEFT JOIN open_pm.cr crPri
ON cr.relate_to_primary_cr = crPri.cr_id
LEFT JOIN open_pm.cr crPre
ON cr.relate_to_pre_approved_cr = crPre.cr_id
LEFT JOIN open_pm.cr_action_code cace
ON cace.cr_action_code_id = cr.cr_return_code_id
LEFT JOIN open_pm.cr_action_code caceResolve
ON caceResolve.cr_action_code_id = cr.cr_return_resolve
LEFT JOIN OPEN_PM.IMPACT_SEGMENT ist
ON cr.IMPACT_SEGMENT = ist.IMPACT_SEGMENT_ID
LEFT JOIN COMMON_GNOC.CFG_CHILD_ARRAY ccay
ON cr.CHILD_IMPACT_SEGMENT = ccay.CHILDREN_ID
LEFT JOIN open_pm.subcategory sy
ON cr.subcategory = sy.subcategory_id
LEFT JOIN open_pm.device_types dts
ON cr.device_type = dts.device_type_id
LEFT JOIN
  (SELECT CR_PROCESS_ID,
    SYS_CONNECT_BY_PATH (CR_PROCESS_NAME, ' ; ') CR_PROCESS_NAME
  FROM open_pm.cr_process
    START WITH PARENT_ID          IS NULL
    CONNECT BY PRIOR CR_PROCESS_ID = PARENT_ID
  ) cps
ON cr.process_type_id = cps.cr_process_id
LEFT JOIN
  (SELECT cr_id,
     rtrim(xmlagg(XMLELEMENT(e,SERVICE_CODE,',').EXTRACT('//text()') ).GetClobVal(),',') AS SERVICE_CODE
  FROM
    (SELECT a.CR_ID,
       b.SERVICE_CODE,
       a.INSERT_TIME
    FROM OPEN_PM.CR_AFFECTED_SERVICE_DETAILS a
    INNER JOIN OPEN_PM.AFFECTED_SERVICES b
    ON a.AFFECTED_SERVICE_ID = b.AFFECTED_SERVICE_ID
    WHERE INSERT_TIME       >= :earliest_start_time
    AND INSERT_TIME         <= :earliest_start_time_to
    )
  GROUP BY cr_id
  ) ass ON cr.cr_id   = ass.cr_id
WHERE usOri.is_enable = 1
