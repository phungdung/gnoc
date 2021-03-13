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
    where LE.APPLIED_SYSTEM = :applied_system and LE.APPLIED_BUSSINESS = :bussiness and LE.LEE_LOCALE = :p_leeLocale
    ),
     list_result AS (
    select
           dts.device_type_id valueStr,
           dts.device_type_code secondValue,
           case
               when llx.LEE_VALUE is null
                     then dts.device_type_name
               else llx.LEE_VALUE end displayStr
    from OPEN_PM.device_types dts
             left join list_language_exchange llx on dts.device_type_id = llx.BUSSINESS_ID
    where dts.is_active = 1
    )
select
    *
from list_result
order by displayStr
