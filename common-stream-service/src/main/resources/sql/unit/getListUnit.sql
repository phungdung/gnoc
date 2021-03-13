SELECT ut.unit_id unitId,
  ut.parent_unit_id parentUnitId,
  ut.DESCRIPTION description,
  ut.STATUS status,
  ut.UNIT_TYPE unitType,
  ut.UNIT_LEVEL unitLevel,
  ut.LOCATION_ID locationId,
  ut.IS_NOC isNoc,
  ut.TIME_ZONE timeZone,
  ut.IS_COMMITTEE isCommittee,
  ut.UPDATE_TIME updateTime,
  ut.SMS_GATEWAY_ID smsGatewayId,
  ut.IPCC_SERVICE_ID ipccServiceId,
  ut.NATION_CODE nationCode,
  ut.NATION_ID nationId,
  ut.MOBILE mobile,
  ut.ROLE_TYPE roleType,
  ut.EMAIL email,
  ut.unit_code unitCode,
  CASE
    WHEN ut.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(ut.unit_code
      || ' ('
      || ut.unit_name
      || ')')
  END AS unitName,
  CASE
    WHEN parentUt.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(parentUt.unit_code
      || ' ('
      || parentUt.unit_name
      || ')')
  END AS parentUnitName
FROM common_gnoc.unit ut
LEFT JOIN common_gnoc.unit parentUt
ON ut.parent_unit_id = parentUt.unit_id
WHERE ut.status      = 1
