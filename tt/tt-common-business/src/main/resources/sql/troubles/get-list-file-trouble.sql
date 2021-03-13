SELECT
  GF.ID id,
  GF.BUSINESS_CODE businessCode,
  GF.BUSINESS_ID businessId,
  GF.PATH path,
  GF.FILE_NAME fileName,
  GF.CREATE_UNIT_ID createUnitId,
  GF.CREATE_UNIT_NAME createUnitName,
  GF.CREATE_USER_ID createUserId,
  GF.CREATE_USER_NAME createUserName,
  GF.CREATE_TIME createTime,
  GF.MAPPING_ID mappingId
FROM COMMON_GNOC.GNOC_FILE GF
WHERE GF.BUSINESS_CODE = :p_business_code AND GF.BUSINESS_ID = :p_business_id
