SELECT LOCATION_ID valueStr,
    LOCATION_CODE secondValue,
    LOCATION_NAME displayStr
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 1
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
