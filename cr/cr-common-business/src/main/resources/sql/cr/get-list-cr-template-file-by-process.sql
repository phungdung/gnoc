WITH
     list_language_exchange AS (
    select
           LE.LEE_ID,
           LE.APPLIED_SYSTEM,
           LE.APPLIED_BUSSINESS,
           LE.BUSSINESS_ID,
           LE.BUSSINESS_CODE,
           LE.LEE_LOCALE,
           LE.LEE_VALUE
    from COMMON_GNOC.LANGUAGE_EXCHANGE LE
    where LE.APPLIED_SYSTEM = :applied_system and LE.APPLIED_BUSSINESS = :bussiness and LE.BUSSINESS_CODE = :business_column and LE.LEE_LOCALE = :p_leeLocale
    ),
     list_result AS (
    SELECT cpte.temp_import_id idTemplate,
           tit.code codeTemplate,
           tit.path linkTemplate,
           cpte.file_type fileType,

           case
                  when llx.LEE_VALUE is null
                     then tit.name
                  else llx.LEE_VALUE end nameTemplate
    FROM cr_process_template cpte
                LEFT JOIN temp_import tit
             on tit.temp_import_id = cpte.temp_import_id
                left join list_language_exchange llx
             on cpte.temp_import_id = llx.BUSSINESS_ID
    WHERE cpte.cr_process_id = :cr_process_id
      AND cpte.file_type       = :file_type
    )
select
    *
from list_result
