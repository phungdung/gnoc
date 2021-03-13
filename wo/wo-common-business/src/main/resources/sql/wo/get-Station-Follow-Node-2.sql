SELECT  a.station_code stationCode
FROM    common_gnoc.infra_stations a
WHERE   lower(a.station_code) = :node
