SELECT s.username itemId,
  s.user_id itemCode,
  (s.fullname
  || ' ('
  || s.username
  || ')') itemName,
  CASE
    WHEN s.email IS NULL
    THEN s.mobile
    ELSE (s.email
      || ' - '
      || s.mobile)
  END itemValue,
  u.unit_id parenItemId,
  u.unit_name parenItemName,
  u.dept_full description
FROM common_gnoc.users s
LEFT JOIN common_gnoc.unit ut
ON s.unit_id = ut.unit_id
LEFT JOIN common_gnoc.v_unit u
ON u.unit_id = s.unit_id
where 1 =1
