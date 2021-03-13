SELECT PRE_CODE_STATION thirdValue,
  LOCATION_ID valueStr,
  LOCATION_CODE secondValue,
  LOCATION_NAME displayStr,
  PARENT_ID
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 1
--   START WITH PARENT_ID  = '289'
  CONNECT BY prior location_id = parent_id
