 with tmp_country as (
SELECT LOCATION_ID, LOCATION_CODE, LOCATION_NAME
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 1
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
),
tmp_area as (
SELECT LOCATION_ID, LOCATION_CODE, LOCATION_NAME, PARENT_ID
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 2
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
)
,
tmp_serial as (
SELECT SERIAL,
  listagg(WO_CODE, ',')  within GROUP (
ORDER BY SERIAL) WO_CODE FROM MR_SCHEDULE_BTS group by serial
),
tmp_province as (
SELECT PRE_CODE_STATION, LOCATION_CODE, LOCATION_NAME, PARENT_ID
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 3
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
)
SELECT T1.DEVICE_ID deviceId,
 T1.MARKET_CODE marketCode,
  T1.PROVINCE_CODE provinceCode,
  T1.STATION_CODE stationCode,
  T2.WO_CODE woCode,
 T1.DEVICE_TYPE deviceType,
 CASE T1.DEVICE_TYPE
  WHEN 'MPD' THEN :deviceTypeMPD
  WHEN 'DH' THEN :deviceTypeDH
 END deviceTypeStr,
  T1.USER_MANAGER userManager,
  T1.UPDATE_USER updateUser,
  T1.SERIAL serial,
 T1.FUEL_TYPE fuelType,
  CASE T1.DEVICE_TYPE
  WHEN 'DH' THEN (
    CASE T1.FUEL_TYPE
      WHEN 'X' THEN :fuelTypeX
      WHEN 'R410a' THEN :fuelTypeR410a
      WHEN 'R410A' THEN :fuelTypeR410A
       WHEN 'R32' THEN :fuelTypeR32
    else :fuelType0 end)
   WHEN 'MPD' THEN (
      CASE T1.FUEL_TYPE
      WHEN 'X' THEN :fuelTypeXX
      WHEN 'D' THEN :fuelTypeD
    else  ''  end)
ELSE '' END fuelTypeName,
  T1.POWER power,
  TO_CHAR(T1.MAINTENANCE_TIME, 'dd/MM/yyyy')  maintenanceTime,
  T1.OPERATION_HOUR operationHour,
  T1.AREA_CODE areaCode,
  T4.LOCATION_NAME areaName,
 T1.PUT_STATUS putStatus,
 CASE T1.PUT_STATUS
  WHEN '0' THEN :putStatus0
  WHEN '1' THEN :putStatus1
  WHEN '2' THEN :putStatus2
 END putStatusStr,
 T1.IN_KTTS inKTTS,
  CASE T1.IN_KTTS
  WHEN '0' THEN :inKTTS0
  WHEN '1' THEN :inKTTS1
 END StrInKTTS,
  T1.PRODUCER producer,
  T1.OPERATION_HOUR_LAST_UPDATE operationHourLastUpdate,
  country.LOCATION_NAME countryName,
  NVL(T3.LOCATION_NAME, T31.LOCATION_NAME) provinceName
FROM MR_DEVICE_BTS T1
LEFT JOIN tmp_serial T2
ON T1.Serial = T2.Serial
LEFT JOIN tmp_country country
on t1.MARKET_CODE = country.location_id
LEFT JOIN tmp_area T4
on (t1.AREA_CODE = t4.LOCATION_CODE AND t4.PARENT_ID = t1.MARKET_CODE)
LEFT JOIN tmp_province T3
on (t1.province_code = t3.PRE_CODE_STATION and t3.PARENT_ID = T4.LOCATION_ID)
LEFT JOIN tmp_province T31
on (t1.province_code = T31.LOCATION_CODE and t31.PARENT_ID = T4.LOCATION_ID)
WHERE 1=1
