SELECT  a.station_code stationCode
FROM    common_gnoc.infra_stations a
WHERE   a.station_id IN (SELECT b.station_id
                          FROM common_gnoc.infra_device b
                          WHERE LOWER(b.device_code) = :node
