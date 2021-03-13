select ut.unit_id itemId, ut.parent_unit_id parenItemId, 
          case when ut.unit_code is null then '' 
          else TO_CHAR(ut.unit_code || ' (' || ut.unit_name || ')') end as itemName,  
          case when parentUt.unit_code is null then '' 
          else TO_CHAR(parentUt.unit_code || ' (' || parentUt.unit_name || ')') end as parenItemName  
          from common_gnoc.unit ut 
          left join common_gnoc.unit parentUt on ut.parent_unit_id = parentUt.unit_id 
          WHERE ut.status = 1 
          and ut.unit_id in ( 
          select ut2.unit_id from common_gnoc.unit ut2
          start with unit_id in (
          select gudl.unit_id from OPEN_PM.group_unit_detail gudl 
          left join OPEN_PM.cr_process_dept_group cpdgp on cpdgp.group_unit_id = gudl.group_unit_id 
          left join OPEN_PM.group_unit gut on cpdgp.group_unit_id = gut.group_unit_id 
          WHERE
          cpdgp.cpdgp_type = 1 
          and gut.is_active = 1
          and cpdgp.cr_process_id = :cr_process_id
          )
          connect by prior unit_id = parent_unit_id
          )
