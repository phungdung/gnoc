select
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
from common_gnoc.gnoc_file GF LEFT JOIN ONE_TM.TROUBLE_FILE_IBM ibm ON gf.MAPPING_ID = ibm.FILE_ID
WHERE 1 = 1
AND gf.BUSINESS_CODE = 'TROUBLE_FILE_IBM'
