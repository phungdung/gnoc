select DISTINCT  a.cr_process_id crProcessId,  b.cpdgp_id cpdgpId, b.group_unit_id groupUnitId, b.cpdgp_type cpdgpType,
c.group_unit_code groupUnitCode, c.group_unit_name groupUnitName,     c.is_active isActive
from cr_process_dept_group b
left join cr_process a on a.cr_process_id = b.cr_process_id
left join group_unit c on b.group_unit_id = c.group_unit_id
where 1 = 1
