SELECT
  a.CR_NUMBER crNumber,
  a.title crName,
  a.CR_ID crId,
  a.STATE state,
  TO_CHAR(a.EARLIEST_START_TIME,'yyyy/mm/dd hh24:mi:ss') earLiestStartTime,
  TO_CHAR(a.LATEST_START_TIME,'yyyy/mm/dd hh24:mi:ss') latestStartTime,
  bd.AFFECTED_SERVICE_ID affectedServiceId,
  b.SERVICE_CODE affectedServiceCode,
  b.SERVICE_NAME affectedServiceName,
  b.DESCRIPTION affectedServiceDescription,
  a.CHANGE_RESPONSIBLE changeResponsibleId,
  c.FULLNAME changeResponsibleFullName,
  c.USERNAME changeResponsibleUserName,
  c.MOBILE changeResponsibleMobile,
  a.CHANGE_RESPONSIBLE_UNIT changeResponsibleUnitId,
  d.UNIT_CODE changeResponsibleUnitCode,
  d.UNIT_NAME changeResponsibleUnitName,
  a.IMPACT_SEGMENT impactSegmentId,
  e.IMPACT_SEGMENT_CODE impactSegmentCode,
  e.IMPACT_SEGMENT_NAME impactSegmentName,
  e.APPLIED_SYSTEM appliedSystem,
  TO_CHAR(a.DISTURBANCE_START_TIME,'yyyy/mm/dd hh24:mi:ss')
  disturbanceStartTime,
  TO_CHAR(a.DISTURBANCE_END_TIME,'yyyy/mm/dd hh24:mi:ss') disturbanceEndTime,
  f.INSERT_TIME insertTime,
  f.DEVICE_ID deviceId,
  f1.DEVICE_CODE deviceCode,
  f1.DEVICE_NAME deviceName,
  f2.IP deviceIp
FROM
  (open_pm.cr a
LEFT JOIN open_pm.cr_affected_service_details bd
ON
  a.CR_ID = bd.CR_ID
LEFT JOIN common_gnoc.users c
ON
  a.CHANGE_RESPONSIBLE = c.USER_ID
LEFT JOIN common_gnoc.unit d
ON
  a.CHANGE_RESPONSIBLE_UNIT = d.UNIT_ID
LEFT JOIN open_pm.impact_segment e
ON
  a.IMPACT_SEGMENT = e.IMPACT_SEGMENT_ID
LEFT JOIN open_pm.cr_impacted_nodes f
ON
  a.CR_ID = f.CR_ID )
LEFT JOIN open_pm.affected_services b
ON
  bd.AFFECTED_SERVICE_ID = b.AFFECTED_SERVICE_ID
LEFT JOIN common_gnoc.infra_device f1
ON
  f.DEVICE_ID = f1.DEVICE_ID
LEFT JOIN common_gnoc.infra_ip f2
ON
  f.DEVICE_ID = f2.DEVICE_ID
WHERE
  a.STATE                 = :state
AND a.EARLIEST_START_TIME > SYSDATE - :param/60/24
