SELECT DISTINCT s.user_id itemId,
  (s.fullname
  || ' ('
  || s.username
  || ')') itemName,
  u.executeUnitId parenItemId,
  u.executeUnitName parenItemName
FROM common_gnoc.users s
LEFT JOIN
  (SELECT unit_id executeUnitId,
    unit_code
    || '('
    ||unit_name
    ||')' executeUnitName
  FROM common_gnoc.unit
  ) u
ON u.executeUnitId = s.UNIT_ID
WHERE s.is_enable  =1
AND 1              =1
