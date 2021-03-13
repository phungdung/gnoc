select dts.device_type_id valueStr,
dts.device_type_name displayStr,
dts.device_type_code secondValue
from device_types dts
where dts.is_active = 1

