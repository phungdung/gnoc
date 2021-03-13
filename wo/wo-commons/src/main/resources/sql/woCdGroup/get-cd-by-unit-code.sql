select wo.WO_GROUP_ID woGroupId, wo.WO_GROUP_CODE woGroupCode, wo.WO_GROUP_NAME woGroupName, wo.EMAIL email
, wo.GROUP_TYPE_ID groupTypeId, wo.IS_ENABLE isEnable ,wo.MOBILE mobile
From Wo_Cd_Group wo where 1=1 and (is_enable = 1 or is_enable is null)
