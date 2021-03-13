SELECT
t1.TEMP_IMPORT_COL_ID tempImportColId,
t1.CODE code,
t1.TITLE title,
t1.IS_MERGE isMerge,
t1.COL_POSITION colPosition,
t1.TEMP_IMPORT_ID tempImportId,
t1.METHOD_PARAMETER_ID methodParameterId,
t1.POS_BK posBk
FROM OPEN_PM.TEMP_IMPORT_COL t1 WHERE t1.TEMP_IMPORT_ID = :tempImportId
