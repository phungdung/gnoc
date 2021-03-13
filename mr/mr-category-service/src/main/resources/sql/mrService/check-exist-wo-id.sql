select w.wo_id woId, m.mr_id mrId 
from WFM.WO w left join OPEN_PM.MR m 
on w.wo_system_id = m.mr_code 
where w.wo_system = 'MR' 
and w.wo_id = :woId
