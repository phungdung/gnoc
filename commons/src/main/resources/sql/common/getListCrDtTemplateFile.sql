select a.CR_DT_TEMPLATE_FILE_ID crDtTemplateFileId ,
 a.CR_PROCESS_ID crProcessId ,
 b.CR_PROCESS_NAME crProcessName ,
 a.FILE_NAME fileName ,
 a.TEMPLATE_TYPE templateType,
 c.CR_PROCESS_ID crProcessParentId ,
 c.CR_PROCESS_NAME crProcessParentName ,
 a.MODIFIED_DATE modifiedDate
 from OPEN_PM.CR_DT_TEMPLATE_FILE a
 LEFT join OPEN_PM.CR_PROCESS b on a.CR_PROCESS_ID = b.CR_PROCESS_ID
 LEFT join OPEN_PM.CR_PROCESS c on b.PARENT_ID = c.CR_PROCESS_ID
 where 1 = 1
