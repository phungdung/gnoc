select us.username as username,
                        us.user_id as userId,
                         ut.unit_id as unitId,
                        ut.unit_Code as unitCode,
                         us.staff_Code as staffCode,
                          us.mobile as mobile,
                        case when us.username is null then ''
                         else TO_CHAR(us.username || '('||us.fullname||')') end as fullname,
                         case when ut.unit_code is null then ''
                         else TO_CHAR(ut.unit_code || '('||ut.unit_name||')') end as unitName
                         from common_gnoc.users us
                          left join common_gnoc.unit ut on ut.unit_id = us.unit_id
                        where ut.status = 1 and us.is_enable = 1
