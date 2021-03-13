
-- Lay danh sach mảng tác động
WITH
list_language_exchange_cat AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = 2 and LE.APPLIED_BUSSINESS = 10 and LE.LEE_LOCALE = :leeLocale
),
list_impact_segment AS (
  select
  ims.IMPACT_SEGMENT_ID impactSegmentId,
  ims.IMPACT_SEGMENT_CODE impactSegmentCode,
  case
    when llec.LEE_VALUE is null
    then ims.IMPACT_SEGMENT_NAME
    else llec.LEE_VALUE end IMPACT_SEGMENT_NAME
  from IMPACT_SEGMENT ims
  left join list_language_exchange_cat llec on ims.IMPACT_SEGMENT_ID = llec.BUSSINESS_ID
),
-- Lay danh sach thiet bi
list_language_exchange_device AS (
   select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = 2 and LE.APPLIED_BUSSINESS = 8 and LE.LEE_LOCALE = :leeLocale
),
list_device_type_cr AS (
  select
    DT.DEVICE_TYPE_ID,
    case
      when llx.LEE_VALUE is null
      then DT.DEVICE_TYPE_NAME
      else llx.LEE_VALUE end DEVICE_TYPE_NAME,
    DT.IS_ACTIVE
  from OPEN_PM.DEVICE_TYPES DT
  left join list_language_exchange_device llx on DT.DEVICE_TYPE_ID = llx.BUSSINESS_ID
),

-- Lay danh sach muc do anh huong
language_exchange_affectlvl AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = 2 and LE.APPLIED_BUSSINESS = 1 and LE.LEE_LOCALE = :leeLocale
),
list_affected_level AS (
  select
  al.affected_level_id affected_level_id,
  case
    when llec.LEE_VALUE is null
    then  al.affected_level_name
    else llec.LEE_VALUE end affected_level_name
  from OPEN_PM.affected_level al
  left join language_exchange_affectlvl llec on al.affected_level_id = llec.BUSSINESS_ID
  WHERE al.is_active    = 1
  AND al.applied_system = 2
),
-- Lay danh sach mã loại hoạt động
list_duty_type as (
  select cife.ife_id valueStr,
       ( nvl(le.LEE_VALUE,cife.ife_name) ||
         ' [' || cife.start_time || 'h00 - ' || cife.end_time || 'h59]') displayStr,
       (cife.start_time || ',' || cife.end_time) secondValue
  from open_pm.cr_impact_frame cife
         left join common_gnoc.language_exchange le
           on cife.ife_id=le.BUSSINESS_ID
                and le.applied_system = 2
                and le.applied_bussiness = 4
                and le.lee_locale = :leeLocale
),
-- :ay danh sach kiểu cr
list_language_exchange_cr AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = 2 and LE.APPLIED_BUSSINESS = 13 and LE.LEE_LOCALE = :leeLocale
),
list_result_cr AS (
  select
    sy.SUBCATEGORY_ID valueStr,
    sy.sy_code secondValue,
    case
      when llx.LEE_VALUE is null
      then sy.sy_name
      else llx.LEE_VALUE end displayStr
  from OPEN_PM.subcategory sy
  left join list_language_exchange_cr llx on sy.SUBCATEGORY_ID = llx.BUSSINESS_ID
  where sy.is_active = 1
),
-- Lấy danh sách dịch vụ ảnh hưởng
language_affected_services AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = 2 and LE.APPLIED_BUSSINESS = 2 and LE.LEE_LOCALE = :leeLocale
),
list_result_affected_services AS (
  select
     ass.affected_service_id valueStr,
     ass.parent_id,
     ass.service_code secondValue,
    case
      when llx.LEE_VALUE is null
      then  to_char(ass.service_name)
      else  to_char(llx.LEE_VALUE) end displayStr
  from OPEN_PM.affected_services ass
  left join language_affected_services llx on ass.affected_service_id = llx.BUSSINESS_ID
  where ass.is_active = 1
),
list_split_affected_services AS (
SELECT PROCEDURE_ID, SERVICE_AFFECTED_ID FROM (
  SELECT PROCEDURE_ID, trim(regexp_substr(SERVICE_AFFECTED_ID, '[^,]+', 1, LEVEL)) SERVICE_AFFECTED_ID
  FROM OPEN_PM.MR_CFG_PROCEDURE_IT_SOFT
  CONNECT BY LEVEL <= regexp_count(SERVICE_AFFECTED_ID, ',') + 1 )
  GROUP BY PROCEDURE_ID, SERVICE_AFFECTED_ID
),
list_result AS (
  select lsas.PROCEDURE_ID, listagg(lras.displayStr,',') within group( order by lras.displayStr ) displayStr
  from list_split_affected_services lsas
  left join list_result_affected_services lras on lsas.SERVICE_AFFECTED_ID = lras.valueStr
  GROUP BY lsas.PROCEDURE_ID
)
SELECT
      CP.PROCEDURE_ID procedureId,
      CP.ARRAY_CODE arrayCode,
      CA.ARRAY_NAME arrayCodeName,
      CP.MARKET_CODE marketCode,
      CP.REGION region,
      CP.DEVICE_TYPE deviceType,
      CP.DEVICE_TYPE_CR deviceTypeCR,
      LDTC.DEVICE_TYPE_NAME deviceTypeCRName,
      CP.IMPORTANT_LEVEL importantLevel,
      CP.MR_CONTENT_ID mrContentId,
      CP.MR_MODE mrMode,
      CP.MR_TYPE mrType,
      CP.CR_TYPE crType,
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
      LIS.IMPACT_SEGMENT_NAME arrayActionName,
      CP.PRIORITY_CR priorityCr,
      CP.TYPE_CR typeCr,
      CP.PROCESS process,
      CP.DUTY_TYPE dutyType,
      LDT.DISPLAYSTR dutyTypeName,
      CP.RISK risk,
      CP.CR cr,
      CP.DESCRIPTION_CR descriptionCr,
      CP.LEVEL_AFFECT levelEffect,
      LAL.AFFECTED_LEVEL_NAME levelEffectName,
      CP.IS_SERVICE_AFFECTED isServiceEffect,
      CP.SERVICE_AFFECTED_ID serviceEffectId,
      lr.displayStr serviceEffectName,
      CP.WO_CONTENT woContent,
      CP.CD_ID cdId,
      CP.PRIORITY_CODE priorityCode,
      CP.IMPACT impact,
      CP.MR_WORKS mrWorks,
      CP.ATTACH_FILE_NAME attachFileName,
      CP.ATTACH_FILE_PATH attachFilePath,
      LO.LOCATION_NAME marketName,
      LRC.DISPLAYSTR crName
 FROM MR_CFG_PROCEDURE_IT_SOFT CP
 LEFT JOIN MR_CAT_ARRAY CA ON CA.ARRAY_CODE=CP.ARRAY_CODE
 left join COMMON_GNOC.CAT_LOCATION lo on LO.LOCATION_ID = CP.MARKET_CODE
left join list_impact_segment lis on LIS.impactSegmentId = cp.array_action
left join list_device_type_cr ldtc on LDTC.DEVICE_TYPE_ID = cp.DEVICE_TYPE_CR
left join list_affected_level lal on lal.AFFECTED_LEVEL_ID = cp.LEVEL_AFFECT
left join list_duty_type ldt on LDT.VALUESTR = cp.DUTY_TYPE
left join list_result_cr lrc on lrc.valueStr = cp.cr
left join list_result lr on lr.PROCEDURE_ID = CP.PROCEDURE_ID
where 1=1
