SELECT a.location_id locationId,
  a.location_code locationCode,
  a.location_name locationName,
  a.description description,
  SYS_CONNECT_BY_PATH(a.location_id, ' / ') locationIdFull,
  SYS_CONNECT_BY_PATH(a.location_name, ' / ') locationNameFull,
  SYS_CONNECT_BY_PATH(a.location_code, ' / ') locationCodeFull,
  a.parent_id parentId,
  level AS locationLevel,
  a.terrain,
  a.location_admin_level locationAdminLevel,
  a.PRE_CODE_STATION preCodeStation,
  a.status status,
  p.location_code parentCode,
  p.location_name parentName,
  a.place,
  a.feature_location featureLocation
FROM common_gnoc.cat_location a
LEFT JOIN common_gnoc.cat_location p
ON p.location_id = a.parent_id
WHERE level      < 50
AND a.status     = 1
