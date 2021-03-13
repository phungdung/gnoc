with
pre_appro as (
select
  '(PRE-APPROVE) ' || cr.title title, cr.cr_id ,
  cr.cr_number ,
  cr.earliest_start_time ,
  cr.latest_start_time ,
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
  cr.change_responsible_unit,
  cr.consider_unit_id,
  cr.consider_user_id
from open_pm.cr cr
left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id
left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id
left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id
left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id
where usOri.is_enable = 1 and (:primary_cr is null or (:primary_cr is not null and cr.relate_to_pre_approved_cr = :primary_cr))
),
secon_dary as(
select
  '(SECONDARY) ' || cr.title title, cr.cr_id ,
  cr.cr_number ,
  cr.earliest_start_time ,
  cr.latest_start_time ,
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
  cr.change_responsible_unit ,
  cr.consider_unit_id ,
  cr.consider_user_id
from open_pm.cr cr
left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id
left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id
left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id
left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id
where usOri.is_enable = 1 and (:primary_cr is null or (:primary_cr is not null and cr.relate_to_primary_cr = :primary_cr))
)
select               pre.title title,
                     pre.cr_id crId,
                     pre.cr_number crNumber,
                     pre.earliest_start_time earliestStartTime,
                     pre.latest_start_time latestStartTime,
                     pre.priority priority ,
                     pre.changeOrginatorUnitName,
                     pre.changeOrginatorName,
                     pre.changeResponsibleUnitName,
                     pre.changeResponsibleName,
                     pre.state state,
                     pre.change_responsible_unit changeResponsibleUnit,
                     pre.consider_unit_id considerUnitId,
                     pre.consider_user_id considerUserId
                     from pre_appro pre
      UNION     select
                     secon.title title,
                     secon.cr_id crId,
                     secon.cr_number crNumber,
                     secon.earliest_start_time earliestStartTime,
                     secon.latest_start_time latestStartTime,
                     secon.priority priority ,
                     secon.changeOrginatorUnitName,
                     secon.changeOrginatorName,
                     secon.changeResponsibleUnitName,
                     secon.changeResponsibleName,
                     secon.state state,
                     secon.change_responsible_unit changeResponsibleUnit,
                     secon.consider_unit_id considerUnitId,
                     secon.consider_user_id considerUserId
                     from secon_dary secon

