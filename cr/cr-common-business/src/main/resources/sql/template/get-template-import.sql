WITH
     list_language_exchange AS (
    select
           LE.LEE_ID,
           LE.APPLIED_SYSTEM,
           LE.APPLIED_BUSSINESS,
           LE.BUSSINESS_ID,
           gf.FILE_NAME BUSSINESS_CODE,
           LE.LEE_LOCALE,
           gf.PATH LEE_VALUE
    from COMMON_GNOC.LANGUAGE_EXCHANGE LE
           join COMMON_GNOC.GNOC_FILE gf on LE.LEE_ID = gf.MAPPING_ID
           join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = 'OPEN_PM') CAT1
             on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
           join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.TEMP_IMPORT') CAT2
             on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
    where LE.LEE_LOCALE = :p_leeLocale AND gf.BUSINESS_CODE = 'TEMP_IMPORT'
    ),
     list_result AS (
    SELECT
           ti.TEMP_IMPORT_ID,
           ti.CODE,
           llx.BUSSINESS_CODE NAME,
           ti.TOTAL_COLUMN,
           ti.TITLE,
           ti.CREATER_ID,
           ti.CREATER_TIME,
           ti.PROCESS_TYPE_ID,
           ti.IS_ACTIVE,
           ti.WEBSERVICE_METHOD_ID,
           ti.IS_VALIDATE_INPUT,
           ti.IS_VALIDATE_OUTPUT,
           ti.IS_REVERT,
           ti.IS_MEC_FILE,
           ti.IS_EDITABLE,
           llx.LEE_VALUE PATH,
           ti.APPLIED_SYSTEM
    FROM OPEN_PM.TEMP_IMPORT ti
           join list_language_exchange llx on ti.TEMP_IMPORT_ID = llx.BUSSINESS_ID
    )
SELECT
       lr.TEMP_IMPORT_ID tempImportId,
       lr.CODE code,
       lr.NAME name,
       lr.TOTAL_COLUMN totalColumn,
       lr.TITLE title,
       lr.CREATER_ID createrId,
       lr.WEBSERVICE_METHOD_ID webServiceMethodId,
       lr.IS_VALIDATE_INPUT isValidateInput,
       lr.IS_VALIDATE_OUTPUT isValidateOutput,
       lr.IS_REVERT isRevert,
       lr.PATH path
FROM list_result lr
WHERE
    lr.TEMP_IMPORT_ID = :temImportId
