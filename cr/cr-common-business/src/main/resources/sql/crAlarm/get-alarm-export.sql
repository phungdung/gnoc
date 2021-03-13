SELECT cr_id crId,
  CREATE_DATE createDate,
  FAULT_SRC faultSrc,
  FAULT_ID faultId,
  FAULT_NAME faultName,
  FAULT_GROUP_ID faultGroupId,
  FAULT_GROUP_NAME faultGroupName,
  DEVICE_TYPE_ID deviceTypeId,
  DEVICE_TYPE_CODE deviceTypeCode,
  VENDOR_CODE vendorCode,
  VENDOR_ID vendorId,
  VENDOR_NAME vendorName,
  NATION_CODE nationCode,
  MODULE_CODE moduleCode,
  MODULE_NAME moduleName,
  KEYWORD keyword
FROM CR_ALARM a
WHERE 1=1 
