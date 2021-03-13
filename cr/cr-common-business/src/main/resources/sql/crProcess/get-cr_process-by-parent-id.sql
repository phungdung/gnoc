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
           join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = :p_system) CAT1
             on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
           join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = :p_bussiness) CAT2
             on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
    where LE.LEE_LOCALE = :p_leeLocale
    ),
     list_data AS
    (select cps.CR_PROCESS_ID crProcessId, cps.CR_PROCESS_NAME crProcessName, impact_type impactType
     from CR_PROCESS cps
     where PARENT_ID = :parentId)
select
       ld.crProcessId,impactType,
       case
         when llx.LEE_VALUE is null
                 then ld.crProcessName
         else llx.LEE_VALUE end crProcessName
from list_data ld
       left join list_language_exchange llx on ld.crProcessId = llx.BUSSINESS_ID
