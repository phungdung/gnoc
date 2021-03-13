select cc.CR_CAB_USERS_ID crCabUsersId,
                    cc.IMPACT_SEGMENT_ID impactSegmentId,
                    cc.EXECUTE_UNIT_ID executeUnitId,
                    cc.CAB_UNIT_ID cabUnitId,
                    cc.USER_ID userID,
                    cc.CREATION_UNIT_ID creationUnitId,
                    ist.impact_segment_name segmentName,
                    u1.unit_code || '('||u1.unit_name||')' executeUnitName,
                    u2.unit_code || '('||u2.unit_name||')' cabUnitName,
                    us.username || '('||us.fullname||')' userFullName,
                    NVL2(u3.unit_code ,u3.unit_code || '('||u3.unit_name||')',null) creationUnitName
                    from open_pm.cr_cab_users cc
                    inner join open_pm.impact_segment ist on cc.impact_segment_id = ist.impact_segment_id
                    inner join common_gnoc.unit u1 on cc.execute_unit_id = u1.unit_id
                    inner join common_gnoc.unit u2 on cc.CAB_UNIT_ID = u2.unit_id
                    inner join common_gnoc.users us on  cc.user_id = us.user_id
                    left join common_gnoc.unit u3 on cc.creation_unit_id = u3.unit_id
                    where 1 = 1
