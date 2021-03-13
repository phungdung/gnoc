SELECT
       a.Id id,
       a.BUSINESS_CODE businessCode,
       a.BUSINESS_ID businessId,
       a.PATH path,
       a.FILE_NAME fileName,
       a.CREATE_UNIT_ID createUnitId,
       a.CREATE_USER_NAME createUserName,
       a.MAPPING_ID mappingId
FROM
     COMMON_GNOC.GNOC_FILE a
WHERE
    1 = 1
