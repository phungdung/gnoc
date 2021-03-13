 select count(w.wo_id) totalRow
FROM wo w,
     wo p,
     wo_type c,
     wo_cd_group g,
     common_gnoc.users f,
     common_gnoc.users cp,
     wo_priority wp,
     wo_detail wd
WHERE 1                = 1
  AND w.wo_type_id       = c.wo_type_id
  AND w.create_person_id = cp.user_id
  AND w.cd_id            = g.wo_group_id
  AND w.ft_id            = f.user_id(+)
  AND w.parent_id        = p.wo_id(+)
  AND w.priority_id      = wp.priority_id(+)
  AND w.wo_id            = wd.wo_id(+)
