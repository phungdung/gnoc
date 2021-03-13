SELECT LOCATION_ID as valueStr,
LOCATION_CODE as displayStr,
LOCATION_NAME as secondValue
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 1
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
