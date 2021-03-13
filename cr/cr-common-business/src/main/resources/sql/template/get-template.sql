SELECT
  vticl.TEMP_IMPORT_COL_ID tempImportColId,
  vticl.code,
  vticl.title,
  vticl.IS_MERGE isMerge,
  vticl.col_position AS colPosition,
  vticl.TEMP_IMPORT_ID tempImportId,
  vticl.METHOD_PARAMETER_ID methodParameterId,
  vticl.PARAMETER_NAME parameterName,
  vticl.DATE_TYPE dataType
FROM
  V_TEMP_IMPORT_COL vticl
WHERE
  vticl.TEMP_IMPORT_ID = :temImportId
ORDER BY
  vticl.col_position
