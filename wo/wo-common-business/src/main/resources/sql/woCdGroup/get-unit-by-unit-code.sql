select unit_id unitId, unit_name unitName, unit_code unitCode,
PARENT_UNIT_ID parentUnitId, location_id locationId from common_gnoc.unit u
where  lower(u.unit_code) =lower(:p_unit_code)
