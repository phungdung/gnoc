WITH LIST_PROVINCE_CODE AS
  (SELECT LOCATION_ID ,
    LOCATION_CODE ,
    LOCATION_NAME ,
    PRE_CODE_STATION
  FROM COMMON_GNOC.cat_location
  WHERE status                   = 1
  AND level                      = 3
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  )
SELECT T1.MR_DEVICE_HIS_ID mrDeviceHisId ,
  T1.MARKET_CODE marketCode ,
  L1.LOCATION_NAME marketCodeStr ,
  T1.AREA_CODE areaCode ,
  L2.LOCATION_NAME areaCodeStr ,
  T1.PROVINCE_CODE provinceCode ,
  L3.LOCATION_NAME provinceCodeStr ,
  T1.DEVICE_TYPE deviceType ,
  T1.DEVICE_ID deviceId ,
  T1.DEVICE_CODE deviceCode ,
  T1.SERIAL serial ,
  T1.CYCLE cycle ,
  T1.COMPLETE_DATE completeDate ,
  T1.WO_CODE woCode ,
  T1.USER_MANAGER userManager ,
  T1.MR_DATE mrDate ,
  T1.STATION_CODE stationCode ,
  T1.IS_COMPLETE isComplete,
  wo_goc.CREATE_DATE      AS createDateGoc ,
  wo_goc.STATUS           AS statusWoGoc ,
  detail.WO_CODE          AS wogiaoLai ,
  wo_giao_lai.STATUS      AS statusWoGL ,
  wo_giao_lai.CREATE_DATE AS createDateGL,
  T1.STATUS status
FROM MR_SCHEDULE_BTS_HIS T1
LEFT JOIN COMMON_GNOC.CAT_LOCATION L1
ON T1.MARKET_CODE = L1.LOCATION_ID
LEFT JOIN COMMON_GNOC.CAT_LOCATION L2
ON T1.AREA_CODE = L2.LOCATION_CODE
LEFT JOIN LIST_PROVINCE_CODE L3
ON T1.PROVINCE_CODE = L3.PRE_CODE_STATION
LEFT JOIN
(
SELECT max(WO_CODE) as WO_CODE, WO_CODE_ORIGINAL
   FROM MR_SCHEDULE_BTS_HIS_DETAIL
   WHERE WO_CODE_ORIGINAL is not null
   GROUP BY WO_CODE_ORIGINAL
) detail
ON T1.WO_CODE = detail.WO_CODE_ORIGINAL
LEFT JOIN
                   (
 SELECT WO_CODE, STATUS ,CREATE_DATE
 FROM WFM.WO
 WHERE 1=1
 AND WO_SYSTEM = 'MR'
 AND WO_SYSTEM_ID like 'MR_BDC_MFD_DH%'
{createDateWo}
) wo_goc
ON T1.WO_CODE = wo_goc.WO_CODE
LEFT JOIN
                   (
 SELECT WO_CODE, STATUS, CREATE_DATE
 FROM WFM.WO
 WHERE 1=1
 AND WO_SYSTEM = 'MR'
 AND WO_SYSTEM_ID like 'MR_BDC_MFD_DH%'
{createDateWo}
) wo_giao_lai
ON detail.WO_CODE = wo_giao_lai.WO_CODE
WHERE 1             = 1
