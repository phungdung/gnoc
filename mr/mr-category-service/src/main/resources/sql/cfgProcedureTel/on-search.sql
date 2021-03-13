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
SELECT
       CP.PROCEDURE_ID procedureId,
       CP.ARRAY_CODE arrayCodeName,
      case
      when llx.LEE_VALUE is null
      then  to_char(CP.ARRAY_CODE)
      else to_char(llx.LEE_VALUE) 
      end arrayCode ,
       CP.MARKET_CODE marketCode,
       CP.DEVICE_TYPE deviceType,
       CP.DEVICE_TYPE_CR deviceTypeCR,
       CP.MR_CONTENT_ID mrContentId,
       CP.MR_MODE mrMode,
       CP.PROCEDURE_NAME procedureName,
       CP.CYCLE cycle,
       CP.CYCLE_TYPE cycleType,
       CP.GEN_MR_BEFORE genMrBefore,
       CP.MR_TIME mrTime,
       CP.GEN_CR genCr,
       CP.GEN_WO genWo,
       CP.RE_GEN_MR_AFTER reGenMrAfter,
       CP.GEN_MR_CR_WO_BY genMrCrWoBy,
       CP.EXP_DATE expDate,
       CP.STATUS status,
       CP.ARRAY_ACTION arrayAction,
       CP.PRIORITY_CR priorityCr,
       CP.TYPE_CR typeCr,
       CP.PROCESS process,
       CP.DUTY_TYPE dutyType,
       CP.RISK risk,
       CP.CR cr,
       CP.DESCRIPTION_CR descriptionCr,
       CP.LEVEL_AFFECT levelEffect,
       CP.IS_SERVICE_AFFECTED isServiceEffect,
       CP.SERVICE_AFFECTED_ID serviceEffectId,
       CP.WO_CONTENT woContent,
       CP.CD_ID cdId,
       CP.PRIORITY_CODE priorityCode,
       CP.IMPACT impact,
       CP.MR_WORKS mrWorks,
       CP.NETWORK_TYPE networkType,
       CP.ARRAY_CHILD arrayChild,
       cl.LOCATION_NAME marketName
  FROM MR_CFG_PROCEDURE_TEL CP
  LEFT JOIN MR_CAT_ARRAY CA ON CA.ARRAY_CODE=CP.ARRAY_CODE
  LEFT JOIN COMMON_GNOC.CAT_LOCATION cl ON (CP.MARKET_CODE  = cl.LOCATION_ID and cl.PARENT_ID is null and cl.status = 1)
  LEFT JOIN COMMON_GNOC.CAT_ITEM it ON (CP.ARRAY_CODE = it.ITEM_VALUE)
  left join list_language_exchange llx on it.item_id = llx.BUSSINESS_ID
)
select
 *
from list_result CP
where 1 = 1
