SELECT
  a.wo_group_name woGroupName,
  a.wo_group_code woGroupCode,
  a.wo_group_id woGroupId
FROM
  wfm.wo_cd_group a
WHERE
  a.group_type_id = 4 AND a.wo_group_id IN
  (SELECT b.cd_group_id
  FROM wfm.wo_cd_group_unit b
  WHERE b.unit_id IN
    (SELECT c.unit_id
    FROM common_gnoc.unit c
      CONNECT BY prior c.unit_id  = c.parent_unit_id
      START WITH c.parent_unit_id =
      (SELECT unit_id FROM common_gnoc.users d WHERE lower(d.username) = :userName
      )
    )
  )
