SELECT ID,
  CODE,
  SQL_TEXT sqlText,
  SYSTEM system,
  STATUS status,
  CREATE_TIME createTime,
  UPDATE_TIME updateTime,
  FORMAT_DATE formatDate,
  WRITE_LOG writeLog,
  COLUMN_KEY columnKey
FROM COMMON_GNOC.CFG_WEB_SERVICE_SQL t1
WHERE 1 = 1
