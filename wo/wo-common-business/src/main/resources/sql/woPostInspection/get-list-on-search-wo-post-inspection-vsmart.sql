SELECT ID id,
  WO_ID woId,
  ACCOUNT account,
  POINT point,
  NOTE note,
  CHECK_USER_NAME checkUserName,
  RECEIVE_USER_NAME receiveUserName,
  RESULT result,
  CREATED_TIME createdTime,
  WO_CODE_PIN woCodePin,
  FILE_NAME filename
FROM WFM.WO_POST_INSPECTION
WHERE 1 = 1
