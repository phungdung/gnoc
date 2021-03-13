 SELECT PRE_CODE_STATION, LOCATION_CODE, LOCATION_NAME, PARENT_ID, LOCATION_ID
FROM common_gnoc.cat_location
WHERE status                   = 1
AND LOCATION_CODE =:provinceCode
AND level                      = 3
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
