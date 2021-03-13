select
  unit_id key,
  unit_id value,
  unit_name title,
  CONNECT_BY_ISLEAF isleaf,
  0 disabled
from common_gnoc.unit
where STATUS = 1
  and ((:p_parent is not null and LEVEL = 2) or (:p_parent is null and parent_unit_id is null))
start with (:p_parent is null and parent_unit_id is null) or (:p_parent is not null and unit_id = :p_parent)
connect by prior unit_id = parent_unit_id
