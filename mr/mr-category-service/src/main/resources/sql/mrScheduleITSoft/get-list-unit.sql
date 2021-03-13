SELECT UNIT.CHECKING_UNIT checkingUnitId,
  UNIT.IMPLEMENT_UNIT implementUnitId
FROM OPEN_PM.MR_CFG_CR_IMPL_UNIT unit
WHERE unit.MARKET_CODE  = :marketCode
AND UNIT.DEVICE_TYPE_ID = :deviceType
AND unit.REGION         = :region
