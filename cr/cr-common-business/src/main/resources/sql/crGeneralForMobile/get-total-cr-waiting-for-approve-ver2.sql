 select count(cr.cr_id) as crId,cr.cr_type as crType 
           from open_pm.cr cr
           where cr.state is not null
           and cr.state = :cr_state 
--          //                     and exists (select 1 from common_gnoc.v_user_role where user_id = ? and role_code = ? )
           and cr.cr_id in (
           with tbl as (
           select d.unit_id,d.cr_id,d.cadt_level,
           (select min(b.cadt_level) from cr_approval_department b
           where  b.cr_id = d.cr_id and b.status = 0) as num
           from cr_approval_department d
           where d.unit_id in (
