WITH tmp_country AS
  (SELECT LOCATION_ID,
    LOCATION_CODE,
    LOCATION_NAME
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 1
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_area AS
  (SELECT LOCATION_ID,
    LOCATION_CODE,
    LOCATION_NAME,
    PARENT_ID
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 2
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_province AS
  (SELECT PRE_CODE_STATION,
    LOCATION_CODE,
    LOCATION_NAME,
    PARENT_ID
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 3
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  )
SELECT t1.LOCATION_CODE marketLocationCode,
  t1.LOCATION_ID marketCode,
  t1.LOCATION_NAME marketName,
  t2.LOCATION_CODE areaCode,
  t2.LOCATION_NAME areaName,
  t3.LOCATION_CODE provinceCode,
  t3.LOCATION_NAME provinceName
FROM tmp_country t1
LEFT JOIN tmp_area t2
ON t1.LOCATION_ID = t2.PARENT_ID
LEFT JOIN tmp_province t3
ON t2.LOCATION_ID     = t3.PARENT_ID
WHERE 1               =1
AND t1.LOCATION_ID   IS NOT NULL
AND t2.LOCATION_CODE IS NOT NULL
-- AND t3.LOCATION_CODE IS NOT NULL
ORDER BY t1.LOCATION_ID,
  t2.LOCATION_CODE,
  t3.LOCATION_CODE
