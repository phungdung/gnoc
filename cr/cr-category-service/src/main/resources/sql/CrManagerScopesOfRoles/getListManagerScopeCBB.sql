select cmse_id cmseId,
cmse_code cmseCode,
cmse_name cmseName
from open_pm.cr_manager_scope
where is_active = 1
order by  cmse_code
