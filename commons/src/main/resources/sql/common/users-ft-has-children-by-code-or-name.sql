with
list_unit_id as (
 select t.unit_id, t.PARENT_UNIT_ID from common_gnoc.unit t
  where level < 50
  start with
  (:p_parent is null and
  t.unit_id  in (
    select g.unit_id
    from wfm.wo_cd_group_unit g, wfm.wo_cd c
    where g.cd_group_id  = c.wo_group_id and c.user_id = :p_userId
  )) or (:p_parent is not null and t.unit_id = :p_parent)
  connect by prior t.unit_id = t.parent_unit_id
)
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
inner join list_unit_id ls on s.unit_id = ls.unit_id
where s.is_enable=1
