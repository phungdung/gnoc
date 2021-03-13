SELECT location_id valueStr, location_name displayStr,PRE_CODE_STATION secondValue, parent_id thirdValue, LEVEL
FROM common_gnoc.cat_location
WHERE 1 = 1  and STATUS = 1
