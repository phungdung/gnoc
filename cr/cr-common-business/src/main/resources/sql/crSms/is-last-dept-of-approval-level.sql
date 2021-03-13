 select cadt_id as cadtId,
                     cr_id as crId,
                     unit_id as unitId,
                     cadt_level as cadtLevel,
                     status as status,
                     user_id as userId,
                     incomming_date incommingDate,
                     approved_date approvedDate,
                     notes notes,
                     return_code returnCode
                     from cr_approval_department 
                     where 
                     cadt_level = :cadt_level
                     and cadt_id <> :cadt_id
                     and cr_id = :cr_id
                     and status = 0 
