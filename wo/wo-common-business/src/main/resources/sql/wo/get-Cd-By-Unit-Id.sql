select  b.wo_group_id woGroupId
from    wo_cd_group b
where   b.wo_group_id in (select a.cd_group_id
                          from wo_cd_group_unit a
                          where a.unit_id = :unitId)
and     group_type_id = :type
