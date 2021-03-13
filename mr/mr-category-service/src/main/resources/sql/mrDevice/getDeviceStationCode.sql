select distinct STATION_CODE stationCode
from OPEN_PM.MR_DEVICE
where STATION_CODE is not null
order by STATION_CODE
