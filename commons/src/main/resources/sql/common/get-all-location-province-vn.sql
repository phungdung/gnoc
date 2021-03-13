SELECT
cl.location_code locationCode,
(cl.location_name || ' (' || cl.location_code || ')') locationName
from common_gnoc.cat_location cl
where cl.parent_id = 282 or cl.parent_id = 283 or cl.parent_id = 284
