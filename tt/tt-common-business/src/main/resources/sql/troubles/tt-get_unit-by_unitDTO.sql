select UNIT_ID unitId,UNIT_NAME unitName,UNIT_CODE unitCode,PARENT_UNIT_ID parentUnitId,DESCRIPTION description,
 STATUS status,UNIT_TYPE unitType,UNIT_LEVEL unitLevel,LOCATION_ID locationId,IS_NOC isNoc,
 TIME_ZONE timeZone,IS_COMMITTEE isCommittee,SMS_GATEWAY_ID smsGatewayId, IPCC_SERVICE_ID ipccServiceId,
 MOBILE mobile, email
from COMMON_GNOC.UNIT  where 1 = 1
