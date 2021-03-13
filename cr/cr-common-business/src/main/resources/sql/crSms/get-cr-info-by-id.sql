select
       cr.CR_ID crId,
       cr.TITLE,
       cr.CR_NUMBER crNumber,
       cr.DESCRIPTION description,
       cr.NOTES notes,
       cace.cr_action_code_tittle crActionCodeTittle,
       caceResolve.cr_action_code_tittle crReturnResolveTitle,
       cr.CR_TYPE crType,
       cr.PRIORITY priority,
       cr.RISK risk,
       cr.STATE state,
       cr.cr_type_cat crTypeCat,
       cr.PROCESS_TYPE_ID processTypeId,
       cr.COUNTRY country,
       cr.REGION region,
       cr.CIRCLE circle,
       cr.manage_unit_id manageUnitId,
       cr.manage_user_id manageUserId,
       cr.SERVICE_AFFECTING serviceAffecting,
       cr.AFFECTED_SERVICE affectedService,
       cr.CHANGE_ORGINATOR changeOrginator,
       cr.CHANGE_ORGINATOR_UNIT changeOrginatorUnit,
       cr.CHANGE_RESPONSIBLE changeResponsible,
       cr.CHANGE_RESPONSIBLE_UNIT changeResponsibleUnit,
       cr.DUTY_TYPE dutyType,
       cr.is_primary_cr isPrimaryCr,
       cr.relate_to_primary_cr relateToPrimaryCr,
       cr.relate_to_pre_approved_cr relateToPreApprovedCr,
       crPri.cr_number relateToPrimaryCrNumber,
       crPre.cr_number relateToPreApprovedCrNumber,
       cr.EARLIEST_START_TIME earliestStartTime,
       cr.LATEST_START_TIME latestStartTime,
       cr.DISTURBANCE_START_TIME disturbanceStartTime,
       cr.DISTURBANCE_END_TIME disturbanceEndTime,
       cr.IMPACT_AFFECT impactAffect,
       cr.IMPACT_SEGMENT impactSegment,
       cr.DEVICE_TYPE deviceType,
       cr.CREATED_DATE createdDate,
       cr.UPDATE_TIME updateTime,
       cr.SUBCATEGORY subcategory,
       case when utConsider.unit_code is null then ''
            else TO_CHAR(utConsider.unit_code || ' ('||utConsider.unit_name||')') end as considerUnitName,
       case when usConsider.username is null then ''
            else TO_CHAR(usConsider.username || ' ('||usConsider.fullname||')') end as considerUserName,
       case when utOri.unit_code is null then ''
            else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName,
       case when usOri.username is null then ''
            else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName,
       case when utResp.unit_code is null then ''
            else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName,
       case when usResp.username is null then ''
            else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName,
       cr.user_cab userCab,
       cr.consider_unit_id considerUnitId, cr.consider_user_id considerUserId
    , cr.WAITING_MOP_STATUS waitingMopStatus , cr.VMSA_VALIDATE_KEY vMSAValidateKey
from open_pm.cr cr
       left join common_gnoc.unit utOri on utOri.unit_id = cr.CHANGE_ORGINATOR_UNIT
       left join common_gnoc.unit utResp on utResp.unit_id = cr.CHANGE_RESPONSIBLE_UNIT
       left join common_gnoc.users usOri on usOri.user_id = cr.CHANGE_ORGINATOR
       left join common_gnoc.users usResp on usResp.user_id = cr.CHANGE_RESPONSIBLE
       left join common_gnoc.unit utConsider on utConsider.unit_id = cr.consider_unit_id
       left join common_gnoc.users usConsider on usConsider.user_id = cr.consider_user_id
       left join open_pm.cr crPri on cr.relate_to_primary_cr = crPri.cr_id
       left join open_pm.cr crPre on cr.relate_to_pre_approved_cr = crPre.cr_id
       left join open_pm.cr_action_code cace on cace.cr_action_code_id = cr.cr_return_code_id
       left join open_pm.cr_action_code caceResolve on caceResolve.cr_action_code_id = cr.cr_return_resolve
where cr.cr_id = :crId
