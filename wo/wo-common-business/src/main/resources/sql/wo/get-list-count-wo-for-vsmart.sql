SELECT g.wo_group_name woGroupName,
  g.wo_group_code woGroupCode,
  COUNT(w.wo_id) total
FROM wfm.wo_cd_group g,
  wfm.wo w
WHERE w.cd_id = g.wo_group_id
AND w.cd_id  IN
  (SELECT a.wo_group_id
  FROM wfm.wo_cd_group a
  WHERE a.group_type_id = 4
  AND a.wo_group_id    IN
    (SELECT b.cd_group_id
    FROM wfm.wo_cd_group_unit b
    WHERE b.unit_id IN
      (SELECT c.unit_id
      FROM common_gnoc.unit c
        CONNECT BY prior c.unit_id        = c.parent_unit_id
        START WITH c.parent_unit_id =
        (SELECT unit_id FROM common_gnoc.users d WHERE d.username = :userName
        )
      )
    )
  )
AND w.status <> 8
AND w.status <> 7
AND (
  CASE
    WHEN (w.status = 2
    AND w.ft_id   IS NULL)
    THEN 0
    ELSE 1
  END )=1
