SELECT DISTINCT TO_CHAR(a.date_off,'dd/MM/yyyy')
  || ' 00:00:00'
FROM COMMON_GNOC.DAY_OFF a
LEFT JOIN common_gnoc.cat_location b
ON a.nation       = b.location_code
WHERE location_id = :locationId
ORDER BY TO_CHAR(a.date_off,'dd/MM/yyyy')
  || ' 00:00:00' DESC
