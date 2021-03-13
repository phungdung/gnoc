SELECT
--CAS_ID casId ,
--  TYPE type ,
  a.NATION_CODE nationCode ,
  a.VENDOR_CODE vendorCode ,
  a.VENDOR_NAME vendorName ,
  a.MODULE_CODE moduleCode ,
  a.MODULE_NAME moduleName ,
--  APPROVAL_STATUS approvalStatus ,
--  APPROVAL_USER_ID approvalUserId ,
  a.FAULT_SRC faultSrc ,
  a.FAULT_ID faultId ,
  a.FAULT_NAME faultName ,
  a.FAULT_GROUP_NAME faultGroupName ,
  a.FAULT_LEVEL_CODE faultLevelCode ,
  a.DEVICE_TYPE_CODE deviceTypeCode ,
--  CR_PROCESS_ID crProcessId,
  a.AUTO_LOAD autoLoad,
  a.CREATED_USER createdUser,
  a.keyword,
  b.DEVICE_TYPE_ID deviceTypeId
  FROM CR_ALARM_SETTTING a
  LEFT JOIN CR_PROCESS b
  ON a.CR_PROCESS_ID = b.CR_PROCESS_ID
WHERE a.APPROVAL_STATUS = 1
