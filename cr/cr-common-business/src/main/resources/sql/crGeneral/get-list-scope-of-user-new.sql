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
           cfg.cmse_id valueStr,
           cfg.cmse_code secondValue,
           case
             when llx.LEE_VALUE is null
                     then cfg.cmse_name
             else llx.LEE_VALUE end displayStr
    from OPEN_PM.cr_manager_scope cfg
           left join list_language_exchange llx on cfg.cmse_id = llx.BUSSINESS_ID
    where cfg.is_active = 1 and cmse_id in (select scope_id from OPEN_PM.v_manage_cr_config where  manage_unit = :manage_unit)
    )
select
       valueStr,
       secondValue,
       displayStr
from list_result
order by displayStr
