select CR_UNITS_SCOPE_ID crUnitsScopeId,
bb.DEVICE_TYPE_ID deviceTypeId,
bb.DEVICE_TYPE_NAME deviceTypeName
from open_pm.CR_UNITS_SCOPE_DEVICE_TYPE aa
inner join open_pm.device_types bb
on aa.DEVICE_TYPE_ID = bb.DEVICE_TYPE_ID
where 1=1 AND CR_UNITS_SCOPE_ID =:cmnoseId
order by CR_UNITS_SCOPE_ID
