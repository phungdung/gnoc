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
SELECT *
FROM
  (SELECT DISTINCT t1.AREA_CODE areaCode,
    t1.CREATED_TIME createdTime,
    t1.DC_POWER dcPower,
    t1.DC_POWER_ID dcPowerId,
    t1.DISCHARGE_CONFIRM dischargeConfirm,
    t1.DISCHARGE_NUMBER dischargeNumber,
    t1.DISCHARGE_REASON_FAIL dischargeReasonFail,
    t1.DISCHARGE_TYPE dischargeType,
    CASE
      WHEN t1.DISCHARGE_TYPE = '2'
      THEN :Isnot
      ELSE :Yes
    END dischargeTypeName,
    t1.DISTRICT_CODE districtCode,
    t1.MR_CODE mrCode,
    t1.WO_CODE woCode,
    t1.PRODUCTION_TECHNOLOGY productionTechnology,
    t1.PROVINCE province,
    t1.PROVINCE_CODE provinceCode,
    t1.REASON_ACCEPT reasonAccept,
    t1.RECENT_DISCHAGE_GNOC recentDischageNoc,
    t1.RECENT_DISCHAGE_NOC recentDischageGnoc,
    ( t1.RECENT_DISCHARGE_CD + :offset * interval '1' hour) recentDischargeCd,
    t1.RECENT_DISCHARGE_GNOC recentDischargeGnoc,
    t1.RECENT_DISCHARGE_NOC recentDischargeNoc,
    t1.RESULT_DISCHARGE resultDischarge,
    t1.STAFF_EMAIL staffMail,
    t1.STAFF_NAME staffName,
    t1.STAFF_PHONE staffPhone,
    t1.STATION_CODE stationCode,
    t1.STATION_ID stationId,
    --   t1.STATUS status,
    t1.STATUS_ACCEPT statusAccept,
    t1.TIME_DISCHARGE timeDischarge,
    t1.MARKET_CODE marketCode,
    t1.ISWOACCU iswoAccu,
    t1.DISTRICT_NAME districtName,
    t2.STATUS statusWo,
    CASE
      WHEN t2.STATUS = 0
      THEN :unassigned
      WHEN t2.STATUS = 1
      THEN :assigned
      WHEN t2.STATUS = 2
      THEN :ftReject
      WHEN t2.STATUS = 3
      THEN :dispatch
      WHEN t2.STATUS = 4
      THEN :accept
      WHEN t2.STATUS = 5
      THEN :inprocess
      WHEN t2.STATUS = 6
      THEN :closeFT
      WHEN t2.STATUS = 7
      THEN :draft
      WHEN t2.STATUS = 8
      THEN :closeCD
      WHEN t2.STATUS = 9
      THEN :pending
      WHEN t2.STATUS = 10
      THEN :cdReject
    END statusWoName,
    CASE
      WHEN t1.ISWOACCU = '0'
      THEN :Isnot
      ELSE :Yes
    END iswoAccuName,
    tmp_country.LOCATION_NAME marketName,
    tmp_area.LOCATION_NAME areaName,
    tmp_province.LOCATION_NAME provinceName,
    0 isImport,
    0 isEdit,
    n1.STATUS statusN,
    CASE
      WHEN n1.STATUS = 1
      THEN :finish
      WHEN n1.STATUS <> 1
      THEN :processing
      WHEN n1.STATUS IS NULL
      THEN ''
    END statusNode,
    NVL(t1.ACTIVE_STATUS,1) activeStatus,
    CASE
      WHEN t1.ACTIVE_STATUS = 1
      THEN :activeStatus1
      WHEN t1.ACTIVE_STATUS = 0
      THEN :activeStatus0
      WHEN t1.STATUS IS NULL
      THEN ''
    END activeStatusName
  FROM MR_CD_BATTERY t1
  JOIN tmp_country
  ON t1.MARKET_CODE = tmp_country.LOCATION_ID
  JOIN tmp_area
  ON t1.AREA_CODE = tmp_area.LOCATION_CODE
  LEFT JOIN wfm.wo t2
  ON t1.WO_CODE = t2.WO_CODE
  LEFT JOIN MR_NODES n1
  ON t2.WO_ID         = n1.WO_ID
  AND t1.STATION_CODE = n1.NODE_IP
  AND t1.DC_POWER     = n1.NODE_NAME
  JOIN tmp_province--chac chan co tinh, lay theo tinh = tinh
  ON t1.PROVINCE_CODE = tmp_province.PRE_CODE_STATION
  JOIN MR_USER_CFG_APPROVED_SMS_BTS mru
  ON tmp_province.LOCATION_CODE = mru.PROVINCE_CODE
  WHERE mru.USERNAME            = :userName
  AND mru.AREA_CODE            <> 'CT'
  UNION
  SELECT DISTINCT t1.AREA_CODE areaCode,
    t1.CREATED_TIME createdTime,
    t1.DC_POWER dcPower,
    t1.DC_POWER_ID dcPowerId,
    t1.DISCHARGE_CONFIRM dischargeConfirm,
    t1.DISCHARGE_NUMBER dischargeNumber,
    t1.DISCHARGE_REASON_FAIL dischargeReasonFail,
    t1.DISCHARGE_TYPE dischargeType,
    CASE
      WHEN t1.DISCHARGE_TYPE = '2'
      THEN :Isnot
      ELSE :Yes
    END dischargeTypeName,
    t1.DISTRICT_CODE districtCode,
    t1.MR_CODE mrCode,
    t1.WO_CODE woCode,
    t1.PRODUCTION_TECHNOLOGY productionTechnology,
    t1.PROVINCE province,
    t1.PROVINCE_CODE provinceCode,
    t1.REASON_ACCEPT reasonAccept,
    t1.RECENT_DISCHAGE_GNOC recentDischageNoc,
    t1.RECENT_DISCHAGE_NOC recentDischageGnoc,
    ( t1.RECENT_DISCHARGE_CD + :offset * interval '1' hour) recentDischargeCd,
    t1.RECENT_DISCHARGE_GNOC recentDischargeGnoc,
    t1.RECENT_DISCHARGE_NOC recentDischargeNoc,
    t1.RESULT_DISCHARGE resultDischarge,
    t1.STAFF_EMAIL staffMail,
    t1.STAFF_NAME staffName,
    t1.STAFF_PHONE staffPhone,
    t1.STATION_CODE stationCode,
    t1.STATION_ID stationId,
    --   t1.STATUS status,
    t1.STATUS_ACCEPT statusAccept,
    t1.TIME_DISCHARGE timeDischarge,
    t1.MARKET_CODE marketCode,
    t1.ISWOACCU iswoAccu,
    t1.DISTRICT_NAME districtName,
    t2.STATUS statusWo,
    CASE
      WHEN t2.STATUS = 0
      THEN :unassigned
      WHEN t2.STATUS = 1
      THEN :assigned
      WHEN t2.STATUS = 2
      THEN :ftReject
      WHEN t2.STATUS = 3
      THEN :dispatch
      WHEN t2.STATUS = 4
      THEN :accept
      WHEN t2.STATUS = 5
      THEN :inprocess
      WHEN t2.STATUS = 6
      THEN :closeFT
      WHEN t2.STATUS = 7
      THEN :draft
      WHEN t2.STATUS = 8
      THEN :closeCD
      WHEN t2.STATUS = 9
      THEN :pending
      WHEN t2.STATUS = 10
      THEN :cdReject
    END statusWoName,
    CASE
      WHEN t1.ISWOACCU = '0'
      THEN :Isnot
      ELSE :Yes
    END iswoAccuName,
    tmp_country.LOCATION_NAME marketName,
    tmp_area.LOCATION_NAME areaName,
    tmp_province.LOCATION_NAME provinceName,
    0 isImport,
    1 isEdit,
    n1.STATUS statusN,
    CASE
      WHEN n1.STATUS = 1
      THEN :finish
      WHEN n1.STATUS <> 1
      THEN :processing
      WHEN n1.STATUS IS NULL
      THEN ''
    END statusNode,
    NVL(t1.ACTIVE_STATUS,1) activeStatus,
    CASE
      WHEN t1.ACTIVE_STATUS = 1
      THEN :activeStatus1
      WHEN t1.ACTIVE_STATUS = 0
      THEN :activeStatus0
      WHEN t1.STATUS IS NULL
      THEN ''
    END activeStatusName
  FROM MR_CD_BATTERY t1
  JOIN tmp_country
  ON t1.MARKET_CODE = tmp_country.LOCATION_ID
  JOIN tmp_area
  ON t1.AREA_CODE = tmp_area.LOCATION_CODE
  LEFT JOIN wfm.wo t2
  ON t1.WO_CODE = t2.WO_CODE
  LEFT JOIN tmp_province--lay khu vuc check, nen tinh = null
  ON t1.PROVINCE_CODE = tmp_province.PRE_CODE_STATION
  LEFT JOIN MR_NODES n1
  ON t2.WO_ID         = n1.WO_ID
  AND t1.STATION_CODE = n1.NODE_IP
  AND t1.DC_POWER     = n1.NODE_NAME
  JOIN MR_USER_CFG_APPROVED_SMS_BTS mru
  ON t1.AREA_CODE        = mru.AREA_CODE
  WHERE mru.USERNAME     = :userName
  AND mru.PROVINCE_CODE IS NULL
  AND mru.AREA_CODE     <> 'CT'
  UNION
  SELECT DISTINCT t1.AREA_CODE areaCode,
    t1.CREATED_TIME createdTime,
    t1.DC_POWER dcPower,
    t1.DC_POWER_ID dcPowerId,
    t1.DISCHARGE_CONFIRM dischargeConfirm,
    t1.DISCHARGE_NUMBER dischargeNumber,
    t1.DISCHARGE_REASON_FAIL dischargeReasonFail,
    t1.DISCHARGE_TYPE dischargeType,
    CASE
      WHEN t1.DISCHARGE_TYPE = '2'
      THEN :Isnot
      ELSE :Yes
    END dischargeTypeName,
    t1.DISTRICT_CODE districtCode,
    t1.MR_CODE mrCode,
    t1.WO_CODE woCode,
    t1.PRODUCTION_TECHNOLOGY productionTechnology,
    t1.PROVINCE province,
    t1.PROVINCE_CODE provinceCode,
    t1.REASON_ACCEPT reasonAccept,
    t1.RECENT_DISCHAGE_GNOC recentDischageNoc,
    t1.RECENT_DISCHAGE_NOC recentDischageGnoc,
    ( t1.RECENT_DISCHARGE_CD + :offset * interval '1' hour) recentDischargeCd,
    t1.RECENT_DISCHARGE_GNOC recentDischargeGnoc,
    t1.RECENT_DISCHARGE_NOC recentDischargeNoc,
    t1.RESULT_DISCHARGE resultDischarge,
    t1.STAFF_EMAIL staffMail,
    t1.STAFF_NAME staffName,
    t1.STAFF_PHONE staffPhone,
    t1.STATION_CODE stationCode,
    t1.STATION_ID stationId,
    --   t1.STATUS status,
    t1.STATUS_ACCEPT statusAccept,
    t1.TIME_DISCHARGE timeDischarge,
    t1.MARKET_CODE marketCode,
    t1.ISWOACCU iswoAccu,
    t1.DISTRICT_NAME districtName,
    t2.STATUS statusWo,
    CASE
      WHEN t2.STATUS = 0
      THEN :unassigned
      WHEN t2.STATUS = 1
      THEN :assigned
      WHEN t2.STATUS = 2
      THEN :ftReject
      WHEN t2.STATUS = 3
      THEN :dispatch
      WHEN t2.STATUS = 4
      THEN :accept
      WHEN t2.STATUS = 5
      THEN :inprocess
      WHEN t2.STATUS = 6
      THEN :closeFT
      WHEN t2.STATUS = 7
      THEN :draft
      WHEN t2.STATUS = 8
      THEN :closeCD
      WHEN t2.STATUS = 9
      THEN :pending
      WHEN t2.STATUS = 10
      THEN :cdReject
    END statusWoName,
    CASE
      WHEN t1.ISWOACCU = '0'
      THEN :Isnot
      ELSE :Yes
    END iswoAccuName,
    tmp_country.LOCATION_NAME marketName,
    tmp_area.LOCATION_NAME areaName,
    tmp_province.LOCATION_NAME provinceName,
    1 isImport,
    1 isEdit,
    n1.STATUS statusN,
    CASE
      WHEN n1.STATUS = 1
      THEN :finish
      WHEN n1.STATUS <> 1
      THEN :processing
      WHEN n1.STATUS IS NULL
      THEN ''
    END statusNode,
    NVL(t1.ACTIVE_STATUS,1) activeStatus,
    CASE
      WHEN t1.ACTIVE_STATUS = 1
      THEN :activeStatus1
      WHEN t1.ACTIVE_STATUS = 0
      THEN :activeStatus0
      WHEN t1.STATUS IS NULL
      THEN ''
    END activeStatusName
  FROM MR_CD_BATTERY t1
  LEFT JOIN tmp_country
  ON t1.MARKET_CODE = tmp_country.LOCATION_ID
  LEFT JOIN tmp_area
  ON t1.AREA_CODE = tmp_area.LOCATION_CODE
  LEFT JOIN wfm.wo t2
  ON t1.WO_CODE = t2.WO_CODE
  LEFT JOIN tmp_province--lay khu vuc check, nen tinh = null
  ON t1.PROVINCE_CODE = tmp_province.PRE_CODE_STATION
  LEFT JOIN MR_NODES n1
  ON t2.WO_ID         = n1.WO_ID
  AND t1.STATION_CODE = n1.NODE_IP
  AND t1.DC_POWER     = n1.NODE_NAME
    --  JOIN MR_USER_CFG_APPROVED_SMS_BTS mru
    --  ON t1.AREA_CODE    = mru.AREA_CODE
  WHERE EXISTS
    (SELECT USERNAME
    FROM MR_USER_CFG_APPROVED_SMS_BTS
    WHERE USERNAME = :userName
    AND AREA_CODE  = 'CT'
    )
  UNION
  SELECT DISTINCT t1.AREA_CODE areaCode,
    t1.CREATED_TIME createdTime,
    t1.DC_POWER dcPower,
    t1.DC_POWER_ID dcPowerId,
    t1.DISCHARGE_CONFIRM dischargeConfirm,
    t1.DISCHARGE_NUMBER dischargeNumber,
    t1.DISCHARGE_REASON_FAIL dischargeReasonFail,
    t1.DISCHARGE_TYPE dischargeType,
    CASE
      WHEN t1.DISCHARGE_TYPE = '2'
      THEN :Isnot
      ELSE :Yes
    END dischargeTypeName,
    t1.DISTRICT_CODE districtCode,
    t1.MR_CODE mrCode,
    t1.WO_CODE woCode,
    t1.PRODUCTION_TECHNOLOGY productionTechnology,
    t1.PROVINCE province,
    t1.PROVINCE_CODE provinceCode,
    t1.REASON_ACCEPT reasonAccept,
    t1.RECENT_DISCHAGE_GNOC recentDischageNoc,
    t1.RECENT_DISCHAGE_NOC recentDischageGnoc,
    ( t1.RECENT_DISCHARGE_CD + :offset * interval '1' hour) recentDischargeCd,
    t1.RECENT_DISCHARGE_GNOC recentDischargeGnoc,
    t1.RECENT_DISCHARGE_NOC recentDischargeNoc,
    t1.RESULT_DISCHARGE resultDischarge,
    t1.STAFF_EMAIL staffMail,
    t1.STAFF_NAME staffName,
    t1.STAFF_PHONE staffPhone,
    t1.STATION_CODE stationCode,
    t1.STATION_ID stationId,
    --   t1.STATUS status,
    t1.STATUS_ACCEPT statusAccept,
    t1.TIME_DISCHARGE timeDischarge,
    t1.MARKET_CODE marketCode,
    t1.ISWOACCU iswoAccu,
    t1.DISTRICT_NAME districtName,
    t2.STATUS statusWo,
    CASE
      WHEN t2.STATUS = 0
      THEN :unassigned
      WHEN t2.STATUS = 1
      THEN :assigned
      WHEN t2.STATUS = 2
      THEN :ftReject
      WHEN t2.STATUS = 3
      THEN :dispatch
      WHEN t2.STATUS = 4
      THEN :accept
      WHEN t2.STATUS = 5
      THEN :inprocess
      WHEN t2.STATUS = 6
      THEN :closeFT
      WHEN t2.STATUS = 7
      THEN :draft
      WHEN t2.STATUS = 8
      THEN :closeCD
      WHEN t2.STATUS = 9
      THEN :pending
      WHEN t2.STATUS = 10
      THEN :cdReject
    END statusWoName,
    CASE
      WHEN t1.ISWOACCU = '0'
      THEN :Isnot
      ELSE :Yes
    END iswoAccuName,
    tmp_country.LOCATION_NAME marketName,
    tmp_area.LOCATION_NAME areaName,
    tmp_province.LOCATION_NAME provinceName,
    0 isImport,
    0 isEdit,
    n1.STATUS statusN,
    CASE
      WHEN n1.STATUS = 1
      THEN :finish
      WHEN n1.STATUS <> 1
      THEN :processing
      WHEN n1.STATUS IS NULL
      THEN ''
    END statusNode,
    NVL(t1.ACTIVE_STATUS,1) activeStatus,
    CASE
      WHEN t1.ACTIVE_STATUS = 1
      THEN :activeStatus1
      WHEN t1.ACTIVE_STATUS = 0
      THEN :activeStatus0
      WHEN t1.STATUS IS NULL
      THEN ''
    END activeStatusName
  FROM MR_CD_BATTERY t1
  LEFT JOIN tmp_country
  ON t1.MARKET_CODE = tmp_country.LOCATION_ID
  LEFT JOIN tmp_area
  ON t1.AREA_CODE = tmp_area.LOCATION_CODE
  LEFT JOIN wfm.wo t2
  ON t1.WO_CODE = t2.WO_CODE
  LEFT JOIN tmp_province--lay khu vuc check, nen tinh = null
  ON t1.PROVINCE_CODE = tmp_province.PRE_CODE_STATION
  LEFT JOIN MR_NODES n1
  ON t2.WO_ID         = n1.WO_ID
  AND t1.STATION_CODE = n1.NODE_IP
  AND t1.DC_POWER     = n1.NODE_NAME
  WHERE NOT EXISTS
    (SELECT USERNAME
    FROM MR_USER_CFG_APPROVED_SMS_BTS
    WHERE USERNAME = :userName
    AND USERNAME  IS NOT NULL
    )
  AND lower(t1.STAFF_EMAIL) LIKE CONCAT(lower(:userName), '@%')
  ) t1
WHERE 1 = 1
