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
    SELECT ac.cr_action_code_id valueStr,
           ac.cr_action_code secondValue,
           case
             when llx.LEE_VALUE is null
                     then ac.cr_action_code_tittle
             else llx.LEE_VALUE end displayStr
    FROM cr_action_code ac
           left join list_language_exchange llx on ac.cr_action_code_id = llx.BUSSINESS_ID
    where ac.is_active = 1
      and (lower(ac.cr_action_code) like :action_code  ESCAPE '\' OR :action_code is null)
    )
select
    *
from list_result order by displayStr
