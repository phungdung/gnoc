select distinct STATION station
from MR_SYN_IT_DEVICES
where STATION is not null
order by STATION
