SELECT WL_TYPE_ID wlTypeId ,
  WL_TYPE_CODE wlTypeCode ,
  WL_TYPE_NAME wlTypeName,
  SR_STATUS srStatus,
  STATUS status,
  CREATED_USER createdUser,
  CREATED_TIME createdTime,
  UPDATED_USER updatedUser,
  UPDATED_TIME updatedTime
FROM OPEN_PM.SR_WORKLOG_TYPE
WHERE 1 = 1
