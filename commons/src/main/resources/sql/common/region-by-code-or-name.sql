SELECT
location_id itemId,
location_code itemCode,
SYS_CONNECT_BY_PATH(location_name, ' / ') itemName,
(location_name || ' (' || location_code || ')') description,
parent_id parenItemId,
LEVEL itemValue
FROM common_gnoc.cat_location
WHERE STATUS = 1
