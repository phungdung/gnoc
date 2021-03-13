select '(PRE-APPROVE) ' || cr.title title, cr.cr_id crId,
                     cr.cr_number crNumber,
                     cr.earliest_start_time earliestStartTime,
                     cr.latest_start_time latestStartTime,
                     cr.priority priority,
                     case when utOri.unit_code is null then ''
                     else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName,
                     case when usOri.username is null then ''
                     else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName,
                     case when utResp.unit_code is null then ''
                     else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName,
                     case when usResp.username is null then ''
                     else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName,
                     cr.state,
                     cr.change_responsible_unit as changeResponsibleUnit,
                     cr.consider_unit_id as considerUnitId,
                     cr.consider_user_id as considerUserId
                     from open_pm.cr cr
                     left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id
                     left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id
                     left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id
                     left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id
                     where usOri.is_enable = 1
