SELECT wo.WO_GROUP_ID woGroupId,
  wo.WO_GROUP_CODE woGroupCode,
  wo.WO_GROUP_NAME woGroupName,
  wo.GROUP_TYPE_ID groupTypeId,
  wo.EMAIL email,
  wo.MOBILE mobile,
  wo.IS_ENABLE isEnable,
  wo.NATION_ID nationId
FROM WO_CD_GROUP wo
WHERE 1=1
