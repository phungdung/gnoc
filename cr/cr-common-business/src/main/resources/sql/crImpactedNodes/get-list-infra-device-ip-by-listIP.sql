SELECT
  vdid.ip_Id ipId,
  vdid.ip ip,
  vdid.device_Id deviceId,
  vdid.device_Code deviceCode,
  vdid.device_Name deviceName,
  CASE
    WHEN vdid.device_Code_Old IS NULL
    THEN ''
    ELSE vdid.device_Code_Old
  END AS deviceCodeOld,
  vdid.network_Type networkType
FROM
  common_gnoc.v_device_ip vdid
WHERE
  vdid.ip IN (:listIP)
  order by vdid.device_Code
