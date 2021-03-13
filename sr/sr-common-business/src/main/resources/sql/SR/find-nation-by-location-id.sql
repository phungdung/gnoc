SELECT
sys_connect_by_path(a.location_id,' / ') country
FROM common_gnoc.cat_location a
LEFT JOIN common_gnoc.cat_location p ON p.location_id = a.parent_id
WHERE 1 = 1
