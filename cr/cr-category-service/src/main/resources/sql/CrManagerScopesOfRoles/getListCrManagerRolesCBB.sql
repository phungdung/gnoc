select cmre_id cmreId,
cmre_code cmreCode,
cmre_name cmreName
from open_pm.cr_manager_role
where is_active = 1
order by  cmre_name
