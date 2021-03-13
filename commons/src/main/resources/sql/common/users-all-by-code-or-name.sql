select distinct
  s.user_id itemId,
  s.username itemCode,
  (s.fullname || ' (' || s.username || ')') itemName,
  case when s.email is null then s.mobile else (s.email || ' - ' || s.mobile) end itemValue,
  u.unit_id parenItemId,
  u.unit_name parenItemName,
  u.dept_full description
from common_gnoc.users s
inner join common_gnoc.v_unit u on s.unit_id = u.unit_id
where (:p_parent is null or (:p_parent is not null and u.unit_id = :p_parent))
