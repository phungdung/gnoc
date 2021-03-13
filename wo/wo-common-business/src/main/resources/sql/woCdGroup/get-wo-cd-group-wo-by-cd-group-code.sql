select
  wo.WO_GROUP_ID woGroupId,
  wo.WO_GROUP_CODE woGroupCode,
  wo.WO_GROUP_NAME woGroupName,
  wo.GROUP_TYPE_ID groupTypeId,
  wo.EMAIL email,
  wo.MOBILE moblie,
  wo.GROUP_TYPE_ID groupTypeId,
  wo.IS_ENABLE isEnable,
  wo.NATION_ID nationId
from
  WO_CD_GROUP wo
where
  1=1
