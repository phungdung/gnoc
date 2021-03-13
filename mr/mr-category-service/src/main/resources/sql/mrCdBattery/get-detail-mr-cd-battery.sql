WITH tmp_country AS
  (SELECT LOCATION_ID,
    LOCATION_CODE,
    LOCATION_NAME
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 1
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_province AS
  (SELECT PRE_CODE_STATION,
    LOCATION_ID,
    LOCATION_CODE,
    LOCATION_NAME,
    PARENT_ID
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 3
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_area AS
  (SELECT LOCATION_ID,
    LOCATION_CODE,
    LOCATION_NAME,
    PARENT_ID
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 2
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_district AS
  (SELECT PRE_CODE_STATION,
    LOCATION_ID,
    LOCATION_CODE,
    LOCATION_NAME,
    PARENT_ID
  FROM common_gnoc.cat_location
  WHERE status                   = 1
  AND level                      = 4
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  )
SELECT  t1.DC_POWER_ID dcPowerId,
     t1.STATION_ID stationId, --them vao dto
     t1.STATION_CODE stationCode,
     t1.DC_POWER dcPower,
     t1.PROVINCE province,
     t1.STAFF_NAME staffName,
     t1.STAFF_EMAIL staffMail,
     t1.STAFF_PHONE staffPhone,
     t1.DISCHARGE_TYPE dischargeType,
     t1.TIME_DISCHARGE timeDischarge,
     (t1.RECENT_DISCHARGE_CD + :offset * interval '1' hour) recentDischargeCd,
     t1.MR_CODE mrCode,
     t1.WO_CODE woCode,
--      t1.STATUS status,
     t1.CREATED_TIME createdTime, --them vao dto
     t1.UPDATED_TIME updatedTime, --them vao dto
     t1.UPDATED_USER updatedUser, --them vao dto
     t1.RECENT_DISCHAGE_NOC recentDischageNoc,
     t1.RECENT_DISCHAGE_GNOC recentDischageGnoc,
     t1.PRODUCTION_TECHNOLOGY productionTechnology,
     t1.DISTRICT_CODE districtCode,
     t1.DISCHARGE_CONFIRM dischargeConfirm,
     t1.RESULT_DISCHARGE resultDischarge,
     t1.DISCHARGE_NUMBER dischargeNumber,
     t1.DISCHARGE_REASON_FAIL dischargeReasonFail,
     t1.RECENT_DISCHARGE_GNOC recentDischargeGnoc,
     t1.RECENT_DISCHARGE_NOC recentDischargeNoc,
     t1.REASON_ACCEPT reasonAccept,
     t1.STATUS_ACCEPT statusAccept,
     t1.AREA_CODE areaCode,
     t1.PROVINCE_CODE provinceCode,
     t1.ISWOACCU iswoAccu,
     t1.MARKET_CODE marketCode,
     t1.DISTRICT_NAME districtName, --them vao dto
     tmp_country.LOCATION_NAME marketName,
     tmp_area.LOCATION_NAME areaName,
     tmp_province.LOCATION_NAME provinceName,
     tmp_area.LOCATION_ID areaId,
     tmp_province.LOCATION_ID provinceId,
     t2.STATUS statusWo
FROM MR_CD_BATTERY t1
LEFT JOIN tmp_country
ON t1.MARKET_CODE = tmp_country.LOCATION_ID
LEFT JOIN tmp_area
ON t1.AREA_CODE = tmp_area.LOCATION_CODE
LEFT JOIN tmp_province
ON t1.PROVINCE_CODE = tmp_province.PRE_CODE_STATION
LEFT JOIN wfm.wo t2
on t1.WO_CODE = t2.WO_CODE
WHERE 1                   =1
