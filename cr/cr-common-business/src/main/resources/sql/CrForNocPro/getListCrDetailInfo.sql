SELECT cr.title title,
  cr.cr_id crId,
  cr.cr_number crNumber,
  cr.DESCRIPTION AS description,
  cr.earliest_start_time earliestStartTime,
  cr.latest_start_time latestStartTime,
  cr.created_date createdDate,
  cr.DISTURBANCE_START_TIME disturbanceStartTime,
  cr.DISTURBANCE_END_TIME disturbanceEndTime,
  cr.priority priority,
  cr.change_orginator_unit AS changeOrginatorUnit,
  CASE
    WHEN utOri.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(utOri.unit_code
      || ' ('
      ||utOri.unit_name
      ||')')
  END                 AS changeOrginatorUnitName,
  cr.change_orginator AS changeOrginator,
  CASE
    WHEN usOri.username IS NULL
    THEN ''
    ELSE TO_CHAR(usOri.username
      || ' ('
      ||usOri.fullname
      ||')')
  END                        AS changeOrginatorName,
  cr.change_responsible_unit AS changeResponsibleUnit,
  CASE
    WHEN utResp.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(utResp.unit_code
      || ' ('
      ||utResp.unit_name
      ||')')
  END                   AS changeResponsibleUnitName,
  cr.change_responsible AS changeResponsible,
  CASE
    WHEN usResp.username IS NULL
    THEN ''
    ELSE TO_CHAR(usResp.username
      || ' ('
      ||usResp.fullname
      || ' - '
      || usResp.MOBILE
      ||')')
  END AS changeResponsibleName,
  (
  CASE cr.cr_type
    WHEN 0
    THEN 'CR Normal'
    WHEN 1
    THEN 'CR Emergency'
    WHEN 2
    THEN 'CR Standard'
    ELSE TO_CHAR(cr.cr_type)
  END) crType,
  ist.impact_segment_id impactSegmentId,
  ist.impact_segment_name impactSegment,
  cr.risk risk,
  cr.RELATE_TO_PRIMARY_CR relateToPrimaryCr,
  cr.RELATE_TO_PRE_APPROVED_CR relateToPreApprovedCr,
  cr.AUTO_EXECUTE autoExecute,
  cr.IS_CONFIRM_ACTION isConfirmAction
FROM open_pm.cr cr
LEFT JOIN common_gnoc.unit utOri
ON cr.change_orginator_unit = utOri.unit_id
LEFT JOIN common_gnoc.users usOri
ON cr.change_orginator = usOri.user_id
LEFT JOIN common_gnoc.unit utResp
ON cr.change_responsible_unit = utResp.unit_id
LEFT JOIN common_gnoc.users usResp
ON cr.change_responsible = usResp.user_id
LEFT JOIN open_pm.device_types dts
ON cr.device_type = dts.device_type_id
LEFT JOIN open_pm.impact_segment ist
ON cr.impact_segment        = ist.impact_segment_id
WHERE cr.STATE             IN (:stateList)
AND cr.CREATED_DATE            > sysdate - 30
AND cr.DISTURBANCE_START_TIME <= :earliestTime
AND cr.DISTURBANCE_END_TIME   >= :latestTime
