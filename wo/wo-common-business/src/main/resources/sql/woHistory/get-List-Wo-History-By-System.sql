SELECT WO_HISTORY_ID woHistoryId,
  OLD_STATUS oldStatus,
  NEW_STATUS newStatus,
  WO_ID woId,
  WO_CODE woCode,
  WO_CONTENT woContent,
  USER_ID userId,
  USER_NAME userName,
  FILE_NAME fileName,
  COMMENTS comments,
  UPDATE_TIME updateTime,
  CREATE_PERSON_ID createPersonId,
  CD_ID cdId,
  FT_ID ftId ,
  IS_SEND_IBM isSendIbm,
  NATION nation
FROM WO_HISTORY
where
1 = 1
