SELECT
  a.title title,
  g.service_name affectedService, --dich vu anh huong
  a.cr_number crNumber,
  a.description description,
  a.notes notes,
  b.affected_level_name impactService,--muc do anh huong
  TO_CHAR(a.earliest_start_time,'dd/MM/yyyy HH24:mi:ss') startTime,
  TO_CHAR(a.latest_start_time,'dd/MM/yyyy HH24:mi:ss') endTime,
  TO_CHAR(a.disturbance_start_time,'dd/MM/yyyy HH24:mi:ss') startAffectedTime,
  TO_CHAR(a.disturbance_end_time,'dd/MM/yyyy HH24:mi:ss') endAffectedTime,
  c.unit_name unitExecuteName,--don vi thuc hien
  d.username createUserName,  --nguoi tao
  e.username executeUserName, --nguoi thuc hien
  i.device_code nodeName,
  i.network_class networkClass,
  TO_CHAR(a.update_time,'dd/MM/yyyy HH24:mi:ss') lastUpdateTime
FROM
  open_pm.cr a
LEFT JOIN open_pm.affected_level b
ON
  a.impact_affect=b.affected_level_id
LEFT JOIN common_gnoc.unit c
ON
  a.change_responsible_unit=c.unit_id
LEFT JOIN common_gnoc.users d
ON
  a.change_orginator=d.user_id
LEFT JOIN common_gnoc.users e
ON
  a.change_responsible=e.user_id
LEFT JOIN open_pm.cr_affected_service_details f
ON
  a.cr_id=f.cr_id
LEFT JOIN open_pm.affected_services g
ON
  f.affected_service_id=g.affected_service_id
LEFT JOIN open_pm.cr_impacted_nodes h
ON
  a.cr_id=h.cr_id
LEFT JOIN common_gnoc.infra_device i
ON
  h.device_id=i.device_id
WHERE
  a.state                 = :state
AND h.insert_time         > TRUNC(a.created_date)
AND a.earliest_start_time > TRUNC(sysdate) - 1
AND a.service_affecting   = 1 --chi lay cac CR co anh huong dich vu
AND a.update_time         > to_date(:lastUpdateTime, 'dd/MM/yyyy HH24:mi:ss')
AND i.network_class      IN ('SITE_ROUTER','SWITCH','DSLAM','GPON_AMP',
  'GPON_OLT')
