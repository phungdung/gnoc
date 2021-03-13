select distinct STATION_CODE stationCode
from MR_DEVICE_CD
where STATION_CODE is not null
order by stationCode
