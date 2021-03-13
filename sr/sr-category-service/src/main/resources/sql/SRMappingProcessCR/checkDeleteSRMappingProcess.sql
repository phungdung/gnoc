select distinct sm.service_code servicecode 
from sr sr
inner join sr_catalog log on sr.service_id = log.service_id
inner join sr_mapping_process_cr sm
on log.service_code    = sm.service_code
where 1=1
