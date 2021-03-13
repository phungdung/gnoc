SELECT DISTINCT b1.COUNTRY country,
  b1.SERVICE_ID serviceId,
  b1.SERVICE_ARRAY serviceArray,
  b1.SERVICE_GROUP serviceGroup,
  b1.SERVICE_CODE serviceCode,
  b1.SERVICE_NAME serviceName,
  b1.SERVICE_DESCRIPTION serviceDescription,
  b1.EXECUTION_UNIT executionUnit,
  un.UNIT_NAME executionUnitDesc,
  b1.REPLY_TIME replyTime,
  b1.EXECUTION_TIME executionTime,
  b1.CR cr,
  b1.WO wo,
  b1.STATUS status,
  b1.IS_INPUT_CHECKING isInputChecking,
  b1.IS_OUTPUT_CHECKING isOutputChecking,
  b1.APPROVE approve,
  b1.ROLE_CODE roleCode,
  b1.FLOW_EXECUTE flowExecute,
  b1.AUTO_CREATE_SR autoCreateSR,
  b1.ATTACH_FILE attachFile,
  b1.IS_ADD_DAY isAddDay,
  b1.RENEW_DAY renewDay,
  b1.CR_WO_CREATE_TIME createdTimeCRWO
FROM OPEN_PM.sr_catalog b1
INNER JOIN OPEN_PM.sr_config b2
ON b1.service_code = b2.config_code
INNER JOIN COMMON_GNOC.UNIT un
ON b1.EXECUTION_UNIT  =un.UNIT_ID
WHERE b2.config_group = :configGroup
