select
ut.unit_id unitId,
ut.parent_unit_id parentUnitId,
ut.unit_code unitCode,
                     case when ut.unit_code is null then ''
                     else TO_CHAR(ut.unit_code || ' (' || ut.unit_name || ')') end as unitName,
                     case when parentUt.unit_code is null then ''
                     else TO_CHAR(parentUt.unit_code || ' (' || parentUt.unit_name || ')') end as parentUnitName
                     from common_gnoc.unit ut
                     left join common_gnoc.unit parentUt on ut.parent_unit_id = parentUt.unit_id
                     where ut.status = 1
