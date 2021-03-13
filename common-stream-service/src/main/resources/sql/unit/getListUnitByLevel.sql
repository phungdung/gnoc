SELECT *
FROM unit
WHERE level                  = :byLevel
  START WITH parent_unit_id IS NULL
  CONNECT BY prior unit_id   = parent_unit_id
ORDER BY level
