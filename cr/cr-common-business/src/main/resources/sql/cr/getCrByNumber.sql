SELECT cr.CR_ID crId,
  cr.TITLE,
  cr.CR_NUMBER crNumber,
  cr.DESCRIPTION description,
  cr.NOTES notes,
  cace.cr_action_code_tittle crActionCodeTittle,
  caceResolve.cr_action_code_tittle crReturnResolveTitle,
  cr.CR_TYPE crType,
  cr.PRIORITY priority,
  cr.RISK risk,
  cr.STATE state,
  cr.cr_type_cat crTypeCat,
  cr.PROCESS_TYPE_ID processTypeId,
  cr.COUNTRY country,
  cr.REGION region,
  cr.CIRCLE circle,
  cr.manage_unit_id manageUnitId,
  cr.manage_user_id manageUserId,
  cr.SERVICE_AFFECTING serviceAffecting,
  cr.AFFECTED_SERVICE affectedService,
  cr.CHANGE_ORGINATOR changeOrginator,
  cr.CHANGE_ORGINATOR_UNIT changeOrginatorUnit,
  cr.CHANGE_RESPONSIBLE changeResponsible,
  cr.CHANGE_RESPONSIBLE_UNIT changeResponsibleUnit,
  cr.DUTY_TYPE dutyType,
  cr.is_primary_cr isPrimaryCr,
  cr.relate_to_primary_cr relateToPrimaryCr,
  cr.relate_to_pre_approved_cr relateToPreApprovedCr,
  crPri.cr_number relateToPrimaryCrNumber,
  crPre.cr_number relateToPreApprovedCrNumber,
  TO_CHAR((cr.EARLIEST_START_TIME ), 'dd/MM/yyyy HH24:mi:ss') earliestStartTime,
  TO_CHAR((cr.LATEST_START_TIME ), 'dd/MM/yyyy HH24:mi:ss') latestStartTime,
  TO_CHAR((cr.DISTURBANCE_START_TIME ), 'dd/MM/yyyy HH24:mi:ss') disturbanceStartTime,
  TO_CHAR((cr.DISTURBANCE_END_TIME ), 'dd/MM/yyyy HH24:mi:ss') disturbanceEndTime,
  cr.IMPACT_AFFECT impactAffect,
  cr.IMPACT_SEGMENT impactSegment,
  cr.CHILD_IMPACT_SEGMENT childImpactSegment,
  cr.DEVICE_TYPE deviceType,
  TO_CHAR((cr.CREATED_DATE ), 'dd/MM/yyyy HH24:mi:ss') createdDate,
  TO_CHAR((cr.UPDATE_TIME ), 'dd/MM/yyyy HH24:mi:ss') updateTime,
  cr.SUBCATEGORY subcategory,
  CASE
    WHEN utConsider.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(utConsider.unit_code
      || ' ('
      ||utConsider.unit_name
      ||')')
  END AS considerUnitName,
  CASE
    WHEN usConsider.username IS NULL
    THEN ''
    ELSE TO_CHAR(usConsider.username
      || ' ('
      ||usConsider.fullname
      ||')')
  END AS considerUserName,
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
  cr.user_cab userCab,
  cr.consider_unit_id considerUnitId,
  cr.consider_user_id considerUserId ,
  cr.WAITING_MOP_STATUS waitingMopStatus ,
  cr.VMSA_VALIDATE_KEY vMSAValidateKey
FROM open_pm.cr cr
LEFT JOIN common_gnoc.unit utOri
ON utOri.unit_id = cr.CHANGE_ORGINATOR_UNIT
LEFT JOIN common_gnoc.unit utResp
ON utResp.unit_id = cr.CHANGE_RESPONSIBLE_UNIT
LEFT JOIN common_gnoc.users usOri
ON usOri.user_id = cr.CHANGE_ORGINATOR
LEFT JOIN common_gnoc.users usResp
ON usResp.user_id = cr.CHANGE_RESPONSIBLE
LEFT JOIN common_gnoc.unit utConsider
ON utConsider.unit_id = cr.consider_unit_id
LEFT JOIN common_gnoc.users usConsider
ON usConsider.user_id = cr.consider_user_id
LEFT JOIN open_pm.cr crPri
ON cr.relate_to_primary_cr = crPri.cr_id
LEFT JOIN open_pm.cr crPre
ON cr.relate_to_pre_approved_cr = crPre.cr_id
LEFT JOIN open_pm.cr_action_code cace
ON cace.cr_action_code_id = cr.cr_return_code_id
LEFT JOIN open_pm.cr_action_code caceResolve
ON caceResolve.cr_action_code_id = cr.cr_return_resolve
WHERE cr.cr_number               = :crNumber
