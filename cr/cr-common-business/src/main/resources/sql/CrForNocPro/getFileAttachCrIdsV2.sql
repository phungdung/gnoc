SELECT DT_CODE dtCode ,
  DT_FILE_HISTORY dtFileHistory,
  FILE_ID fileId,
  FILE_NAME fileName,
  CR_ID crId
FROM OPEN_PM.CR_FILES_ATTACH
where ( CR_ID in (:crId0)
