SELECT a.FILE_ID fileId,
  a.FILE_NAME fileName,
  a.FILE_PATH filePath,
  a.FILE_GROUP fileGroup,
  a.OBEJCT_ID obejctId,
  a.FILE_TYPE fileType,
  a.REQUIRE_CREATE_SR requireCreateSR
FROM OPEN_PM.SR_FILES a
WHERE 1 = 1
