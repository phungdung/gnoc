SELECT gte.timezone_offset timeOffset
FROM Common_gnoc.USERS us
LEFT JOIN common_gnoc.unit ut
ON ut.unit_id = us.unit_id
LEFT JOIN Common_gnoc.gnoc_timezone gte
ON gte.GNOC_TIMEZONE_ID  = ut.time_zone
WHERE lower(us.username) = :userName
