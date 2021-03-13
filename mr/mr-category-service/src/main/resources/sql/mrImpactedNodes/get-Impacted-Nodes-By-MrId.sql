SELECT
  mins.mins_id minsId,
  mins.ip_id ipId,
  iip.ip ip,
  ide.device_name deviceName,
  ide.device_code deviceCode,
  ide.device_id deviceId,
  ide.device_code_old deviceCodeOld
FROM
  open_pm.mr_impacted_nodes mins
LEFT JOIN common_gnoc.infra_ip iip
ON
  iip.ip_id = mins.ip_id
LEFT JOIN common_gnoc.infra_device ide
ON
  ide.device_id = mins.device_id
WHERE
  mr_id = :mrId
