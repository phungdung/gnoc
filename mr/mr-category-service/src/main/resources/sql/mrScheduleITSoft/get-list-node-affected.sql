SELECT dev.UD ud,
  dev.DB db,
  DEV.BO_UNIT boUnit,
  dev.APPROVE_STATUS approveStatus,
  dev.OBJECT_ID objectId,
  DEV.NODE_AFFECTED nodeAffected,
  dev.LEVEL_IMPORTANT levelImportant,
  dev.MR_CONFIRM_SOFT mrConfirm
FROM OPEN_PM.MR_SYN_IT_DEVICES dev
WHERE dev.MARKET_CODE = :marketCode
AND dev.DEVICE_TYPE   = :deviceType
AND dev.OBJECT_ID     = :objectId
