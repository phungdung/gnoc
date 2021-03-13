SELECT DISTINCT TO_CHAR(g.WO_GROUP_ID) woGroupId,
  g.WO_GROUP_CODE woGroupCode,
  g.WO_GROUP_NAME woGroupName
FROM wfm.wo_cd_group g,
  wfm.wo_cd_group_unit u
WHERE g.wo_group_id = u.cd_group_id
AND g.group_type_id = 4
AND u.unit_id      IN
  (SELECT unit_id
  FROM common_gnoc.unit b
    CONNECT BY prior b.unit_id = b.parent_unit_id
    START WITH b.unit_id      IN
    (SELECT unit_id
    FROM wfm.wo_cd_group_unit
    WHERE cd_group_id IN
      (SELECT CD_ID
      FROM MAP_PROVICE_CD
      WHERE lower(LOCATION_CODE) LIKE :locationCode ESCAPE '\'
      )
    )
  )
