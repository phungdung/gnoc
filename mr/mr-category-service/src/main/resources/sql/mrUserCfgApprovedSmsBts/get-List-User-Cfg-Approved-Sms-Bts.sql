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
),
tmp_province as (
SELECT PRE_CODE_STATION, LOCATION_CODE, LOCATION_NAME, PARENT_ID
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 3
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
)
select t1.USER_CFG_APPROVED_SMS_ID userCfgApprovedSmsId,
t1.MARKET_CODE marketCode,
t1.AREA_CODE areaCode,
t1.PROVINCE_CODE provinceCode,
t1.USERNAME userName,
t1.MOBILE mobile,
t1.APPROVE_LEVEL approveLevel,
t1.FULLNAME fullName,
t1.USERS_ID userID,
t1.CREATE_TIME createTime,
t1.UPDATE_TIME updateTime,
t1.USER_UPDATE userUpdate,
t1.RECEIVE_MESSAGE_BD receiveMessageBD,
t2.LOCATION_NAME marketName,
t3.LOCATION_NAME areaName,
t4.LOCATION_NAME provinceName,
t4.PRE_CODE_STATION preCodeStation,
CASE t1.APPROVE_LEVEL
  WHEN 1 THEN :approveLV1
  WHEN 2 THEN :approveLV2
  WHEN 3 THEN :approveLV1andLV2
 END approveLevelStr,
 CASE t1.RECEIVE_MESSAGE_BD
  WHEN 0 THEN :receiveMessage0
  WHEN 1 THEN :receiveMessage1
 END receiveMessageBDStr
from MR_USER_CFG_APPROVED_SMS_BTS t1
LEFT JOIN tmp_country t2
on t1.MARKET_CODE = t2.location_id
LEFT JOIN tmp_area t3
on (t1.AREA_CODE = t3.LOCATION_CODE AND t3.PARENT_ID = t1.MARKET_CODE)
LEFT JOIN tmp_province t4
on (t1.PROVINCE_CODE = t4.LOCATION_CODE and t4.PARENT_ID = T3.LOCATION_ID)
WHERE 1 = 1
