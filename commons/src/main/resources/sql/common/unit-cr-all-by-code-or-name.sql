select ut.unit_id itemId, ut.parent_unit_id parenItemId,
          case when ut.unit_code is null then ''
          else TO_CHAR(ut.unit_code || ' (' || ut.unit_name || ')') end as itemName,
          case when parentUt.unit_code is null then ''
          else TO_CHAR(parentUt.unit_code || ' (' || parentUt.unit_name || ')') end as parenItemName
          from common_gnoc.unit ut
          left join common_gnoc.unit parentUt on ut.parent_unit_id = parentUt.unit_id
          WHERE ut.status = 1
