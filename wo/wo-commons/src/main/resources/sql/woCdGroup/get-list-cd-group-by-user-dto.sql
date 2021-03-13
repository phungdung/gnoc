SELECT g.wo_group_id woGroupId ,
  g.wo_group_code woGroupCode ,
  g.wo_group_name woGroupName ,
  g.email ,
  g.mobile ,
  g.group_type_id groupTypeId ,
  g.is_enable isEnable
FROM wo_cd_group g
WHERE 1       = 1
AND is_enable = 1
