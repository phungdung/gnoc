select
  a.id id,
  a.reason_name reasonName,
  a.parent_id parentId,
  a.description description,
  a.type_id typeId,
  a.reason_code reasonCode,
  a.reason_type reasonType,
  a.update_time updateTime
from
  one_tm.cat_reason a
