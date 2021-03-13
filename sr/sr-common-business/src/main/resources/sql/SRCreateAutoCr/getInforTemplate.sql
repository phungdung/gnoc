WITH
list_language_exchange AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE,
    GN.PATH,
    GN.FILE_NAME
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = 'OPEN_PM') CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.TEMP_IMPORT') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  join (select * from COMMON_GNOC.GNOC_FILE where BUSINESS_CODE='TEMP_IMPORT') GN
    on GN.BUSINESS_ID = LE.BUSSINESS_ID and LE.LEE_ID = GN.MAPPING_ID
  where LE.LEE_LOCALE = :p_leeLocale
),
list_result AS (
  SELECT
    ti.TEMP_IMPORT_ID,
    llx.FILE_NAME name,
    llx.PATH path
  FROM OPEN_PM.TEMP_IMPORT ti
  left join list_language_exchange llx on ti.TEMP_IMPORT_ID = llx.BUSSINESS_ID
)
SELECT
  pro.cr_process_id crProcessId,
  pro.temp_import_id tempImportId,
  pro.file_type fileType,
  temp.name ,
  temp.path pathFileProcess
FROM OPEN_PM.CR_PROCESS_TEMPLATE pro
INNER JOIN list_result temp
ON pro.temp_import_id   = temp.temp_import_id
WHERE pro.cr_process_id = :p_cr_process_id
AND pro.file_type       = 101
