WITH list_language_exchange_cat AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  WHERE LE.APPLIED_SYSTEM  = 1
  AND LE.APPLIED_BUSSINESS = 3
  AND LE.LEE_LOCALE        = :p_leeLocale
  ),
  list_unit_type AS
  (SELECT ci.item_id,
    ci.item_code,
    CASE
      WHEN llec.LEE_VALUE IS NULL
      THEN ci.item_name
      ELSE llec.LEE_VALUE
    END item_name
  FROM common_gnoc.cat_item ci
  LEFT JOIN list_language_exchange_cat llec
  ON ci.item_id        = llec.BUSSINESS_ID
  WHERE ci.category_id =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='UNIT_TYPE'
    AND EDITABLE       = 1
    )
  ),
  list_unit_level AS
  (SELECT ci.item_id,
    ci.item_code,
    CASE
      WHEN llec.LEE_VALUE IS NULL
      THEN ci.item_name
      ELSE llec.LEE_VALUE
    END item_name
  FROM common_gnoc.cat_item ci
  LEFT JOIN list_language_exchange_cat llec
  ON ci.item_id        = llec.BUSSINESS_ID
  WHERE ci.category_id =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='UNIT_LEVEL'
    AND EDITABLE       = 1
    )
  ) ,
  list_role_list AS
  (SELECT ci.item_id,
    ci.item_code,
    CASE
      WHEN llec.LEE_VALUE IS NULL
      THEN ci.item_name
      ELSE llec.LEE_VALUE
    END item_name
  FROM common_gnoc.cat_item ci
  LEFT JOIN list_language_exchange_cat llec
  ON ci.item_id        = llec.BUSSINESS_ID
  WHERE ci.category_id =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='ROLE_LIST'
    AND EDITABLE       = 1
    )
  ),
  list_sms_gate_way AS
  (SELECT * FROM COMMON_GNOC.SMS_GATEWAY sgw
  ),
  list_ipcc AS
  (SELECT a.ipcc_service_id ,
    a.ipcc_service_code
  FROM COMMON_GNOC.ipcc_service a
  ),
  list_location AS
  (SELECT *
  FROM COMMON_GNOC.cat_location
  WHERE status                   = 1
  AND level                      < 10
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ORDER BY level
  )
SELECT ut.unit_id unitId,
  ut.parent_unit_id parentUnitId,
  ut.DESCRIPTION description,
  ut.STATUS status,
  ut.UNIT_TYPE unitType,
  lut.item_name unitTypeStr,
  ut.UNIT_LEVEL unitLevel,
  lul.item_name unitLevelStr,
  smsGt.ALIAS smsGatewayName,
  lrole.item_name roleTypeName,
  lipcc.ipcc_service_code ipccServiceName,
  ut.LOCATION_ID locationId,
  lcation.LOCATION_NAME locationName,
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
  ut.unit_name unitName,
  parentUt.unit_name parentUnitName
--   CASE
--     WHEN ut.unit_code IS NULL
--     THEN ''
--     ELSE TO_CHAR(ut.unit_code
--       || ' ('
--       || ut.unit_name
--       || ')')
--   END AS unitName,
--   CASE
--     WHEN parentUt.unit_code IS NULL
--     THEN ''
--     ELSE TO_CHAR(parentUt.unit_code
--       || ' ('
--       || parentUt.unit_name
--       || ')')
--   END AS parentUnitName
FROM common_gnoc.unit ut
LEFT JOIN common_gnoc.unit parentUt
ON ut.parent_unit_id = parentUt.unit_id
LEFT JOIN list_unit_type lut
ON lut.item_id = ut.UNIT_TYPE
LEFT JOIN list_unit_level lul
ON lul.item_id = ut.UNIT_LEVEL
LEFT JOIN list_role_list lrole
ON lrole.item_id = ut.ROLE_TYPE
LEFT JOIN list_sms_gate_way smsGt
ON smsGt.SMS_GATEWAY_ID = ut.SMS_GATEWAY_ID
LEFT JOIN list_ipcc lipcc
ON lipcc.ipcc_service_id = ut.IPCC_SERVICE_ID
LEFT JOIN list_location lcation
ON lcation.LOCATION_ID = ut.LOCATION_ID
WHERE 1=1
