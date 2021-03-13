SELECT
d.network_type networkType ,
d.network_class networkClass ,
s.station_name stationName,
d.device_code deviceCode,
d.device_name deviceName,
TO_CHAR(d.create_date, 'dd/MM/yyyy HH24:mi:ss') creatDate,
ip.ip ipNode
FROM COMMON_GNOC.infra_device d
LEFT JOIN COMMON_GNOC.infra_stations s
ON d.station_id = s.station_id
INNER JOIN COMMON_GNOC.infra_ip ip
ON d.device_id = ip.device_id
WHERE 1 = 1
