SELECT
location_code itemId,
location_id itemCode,
location_name itemName,
LEVEL
FROM common_gnoc.cat_location
WHERE STATUS = 1
