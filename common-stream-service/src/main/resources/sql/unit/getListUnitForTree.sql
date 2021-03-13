SELECT a.unit_id unitId,
  a.unit_name unitName,
  a.unit_code unitCode,
  a.parent_unit_id parentUnitId,
  a.status,
  a.description,
  a.unit_type unitType,
  a.unit_level unitLevel,
  a.location_id locationId,
  a.is_noc isNoc,
  a.time_zone timeZone,
  a.is_committee isCommittee,
  a.update_time updateTime,
  a.sms_gateway_id smsGatewayId
FROM unit a
WHERE 1=1
