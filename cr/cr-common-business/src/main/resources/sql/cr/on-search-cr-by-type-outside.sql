select cr.title title, cr.cr_id crId,
                              cr.cr_number crNumber,
                              (cr.update_time + :offset * interval '1' hour) updateTime,
                              (cr.earliest_start_time + :offset * interval '1' hour) earliestStartTime,
                              (cr.latest_start_time + :offset * interval '1' hour) latestStartTime,
                              (cr.created_date + :offset * interval '1' hour) createdDate,
                              cr.priority priority,
                              case when utOri.unit_code is null then ''
                              else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName,
                              case when usOri.username is null then ''
                              else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName,
                              case when utResp.unit_code is null then ''
                              else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName,
                              case when usResp.username is null then ''
                              else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName,
                              case when utConsi.unit_code is null then ''
                              else TO_CHAR(utConsi.unit_code || ' ('||utConsi.unit_name||')') end as considerUnitName,
                              case when usConsi.username is null then ''
                              else TO_CHAR(usConsi.username || ' ('||usConsi.fullname||')') end as considerUserName,
                              cr.state,
                              cr.cr_type_cat crTypeCat,
                              cr.relate_to_pre_approved_cr relateToPreApprovedCr,
                              cr.cr_type crType,
                              cr.change_responsible as changeResponsible,
                              cr.change_responsible_unit as changeResponsibleUnit,
                              cr.change_orginator as changeOrginator,
                              cr.change_orginator_unit as changeOrginatorUnit,
                              cr.consider_unit_id as considerUnitId,
                              cr.consider_user_id as considerUserId,
                              cr.IMPACT_SEGMENT impactSegment,
                              cr.CHILD_IMPACT_SEGMENT childImpactSegment,
                              case cr.state
                              when :state_close
                              then
                              case
                              when exists (select 1 from
                              open_pm.cr_his chs1
                              where cr.cr_id = chs1.cr_id and chs1.action_code in (:action_reject,:act_close,:act_close_by_appr,:act_close_by_man,:act_close_by_emer)) then ''
                              else   TO_CHAR(((select max(chs2.change_date) from open_pm.cr_his chs2 where cr.cr_id = chs2.cr_id
                              and chs2.change_date >= cr.created_date
                              and chs2.action_code in (:act_close_cr,:act_close_cr_appr,:act_close_excu))
                              + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') end
                              when :state_draft then ''
                              else to_char(sysdate + :offset * interval '1' hour, 'dd/MM/yyyy HH24:mi:ss')
                              end as compareDate,
                              cr.user_cab userCab, TO_CHAR(cr.AUTO_EXECUTE) as autoExecute, cr.respone_time responeTime,
                              cr.RANK_GATE rankGate, cr.IS_RUN_TYPE isRunType
                              from open_pm.cr cr
                              left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id
                              left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id
                              left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id
                              left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id
                              left join common_gnoc.unit utConsi on cr.consider_unit_id = utConsi.unit_id
                              left join common_gnoc.users usConsi on cr.consider_user_id = usConsi.user_id
                              where usOri.is_enable = 1
