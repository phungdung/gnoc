SELECT a.wo_type_id woTypeId,
  a.wo_type_code woTypeCode,
  wo_type_name woTypeName,
  a.IS_ENABLE isEnable,
  a.WO_GROUP_TYPE woGroupType,
  a.ENABLE_CREATE enableCreate,
  a.TIME_OVER timeOver,
  a.SMS_CYCLE smsCycle,
  a.WO_CLOSE_AUTOMATIC_TIME woCloseAutomaticTime,
  a.ALLOW_PENDING allowPending,
  a.USER_TYPE_CODE userTypeCode,
  a.CREATE_FROM_OTHER_SYS createFromOtherSys,
  a.TIME_AUTO_CLOSE_WHEN_OVER timeAutoCloseWhenOver
FROM wo_type a
WHERE lower(a.wo_type_code) =lower(:p_type_code)
