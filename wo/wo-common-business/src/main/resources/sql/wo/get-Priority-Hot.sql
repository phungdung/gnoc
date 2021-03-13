select  a.priority_id
from    wfm.wo_priority a
where   a.wo_type_id = (select b.wo_type_id from wfm.wo_type b where b.wo_type_code = :woTypeCode)
and     a.priority_code = :priorityCode
