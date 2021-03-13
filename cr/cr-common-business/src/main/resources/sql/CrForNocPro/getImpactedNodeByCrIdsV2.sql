SELECT impn.CR_ID crId ,
  impn.ip_id ipId ,
  impn.ip ip,
  impn.device_id deviceId ,
  impn.device_code deviceCode ,
  TO_CHAR(impn.device_name) deviceName,
  DECODE(impn.NATION_CODE,NULL,'VNM',impn.NATION_CODE) nationCode
FROM CR_IMPACTED_NODES impn
WHERE ( impn.CR_ID IN (:crId0)
