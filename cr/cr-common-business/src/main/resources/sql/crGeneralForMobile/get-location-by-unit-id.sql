SELECT a.unit_id unitId,
  b.location_code locationCode
FROM common_gnoc.unit a,
  common_gnoc.cat_location b
WHERE a.location_id=b.location_id
AND unit_id        = :unit_id
