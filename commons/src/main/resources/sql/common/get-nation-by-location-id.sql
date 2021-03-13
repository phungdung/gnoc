SELECT
  LOCATION_ID locationId,
  LOCATION_CODE locationCode,
  LOCATION_NAME locationName,
  DESCRIPTION description,
  PARENT_ID parentId,
  PARENT_CODE parentCode,
  LOCATION_ADMIN_LEVEL locationAdminLevel,
  TERRAIN terrain, PRE_CODE_STATION preCodeStation,
  STATUS,
  PLACE,
  FEATURE_LOCATION featureLocation,
  LAST_UPDATE_TIME lastUpdateTime,
  NATION_ID nationId,
  NATION_CODE nationCode
FROM common_gnoc.CAT_LOCATION
where 1 = 1  and parent_id is null and status = 1
connect by PRIOR PARENT_ID=LOCATION_ID
START WITH LOCATION_ID = :locationId
