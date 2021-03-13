WITH lst_lang AS
  (SELECT
    CASE
      WHEN l.LEE_VALUE IS NULL
      THEN c.ITEM_NAME
      ELSE l.LEE_VALUE
    END LEE_VALUE,
    c.ITEM_CODE ITEM_CODE
  FROM COMMON_GNOC.CAT_ITEM c
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE l
  ON c.ITEM_ID         = l.BUSSINESS_ID
  AND APPLIED_BUSSINESS=3
  AND l.APPLIED_SYSTEM =1
  AND l.LEE_LOCALE     =:lee_Locale
  WHERE c.STATUS       = 1
  AND c.CATEGORY_ID    =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='MR_SUBCATEGORY'
    AND STATUS         = 1
    )
  ORDER BY position,
    NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese' )
  ),
  language_mr_works AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = :applied_system and LE.APPLIED_BUSSINESS = :bussiness and LE.LEE_LOCALE = :lee_Locale
),
list_result_mr_works AS (
  select
     mrWorkdList.item_value itemValue,
     mrWorkdList.item_code itemCode,
     mrWorkdList.item_id itemId,
    case
      when llx.LEE_VALUE is null
      then mrWorkdList.item_name
      else llx.LEE_VALUE end displayStr
  from (select * from COMMON_GNOC.CAT_ITEM
        where STATUS = 1 and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode and STATUS = 1)) mrWorkdList
  left join language_mr_works llx on mrWorkdList.item_id = llx.BUSSINESS_ID
),
list_split_mr_works  AS (
SELECT PROCEDURE_ID, MR_WORKS FROM (
  SELECT PROCEDURE_ID, trim(regexp_substr(MR_WORKS, '[^,]+', 1, LEVEL)) MR_WORKS
  FROM OPEN_PM.MR_CFG_PROCEDURE_IT_HARD
  CONNECT BY LEVEL <= regexp_count(MR_WORKS, ',') + 1 )
  GROUP BY PROCEDURE_ID, MR_WORKS
),
list_result AS (
  select lsas.PROCEDURE_ID, listagg(lras.displayStr,',') within group( order by lras.displayStr ) displayStr
  from list_split_mr_works lsas
  left join list_result_mr_works lras on lsas.MR_WORKS = lras.itemId
  GROUP BY lsas.PROCEDURE_ID
)
SELECT CP.PROCEDURE_ID procedureId,
  CP.ARRAY_CODE arrayCode,
  CASE
    WHEN ll.LEE_VALUE IS NULL
    THEN CP.ARRAY_CODE
    ELSE TO_CHAR(ll.LEE_VALUE)
  END arrayCodeName,
  CP.MARKET_CODE marketCode,
  cl.location_name marketName,
  CP.REGION region,
  CP.DEVICE_TYPE deviceType,
  CP.DEVICE_TYPE_CR deviceTypeCR,
  CP.MR_CONTENT_ID mrContentId,
  CP.MR_MODE mrMode,
  CASE
    WHEN CP.MR_MODE = 'H'
    THEN :Hard
    ELSE :Soft
  END mrModeName,
  CP.PROCEDURE_NAME procedureName,
  CP.CYCLE cycle,
  CP.CYCLE_TYPE cycleType,
  CASE
    WHEN CP.CYCLE_TYPE = 'M'
    THEN :month
    ELSE :day
  END cycleTypeName,
  CP.GEN_MR_BEFORE genMrBefore,
  CP.MR_TIME mrTime,
  CP.GEN_CR genCr,
  CP.GEN_WO genWo,
  CP.RE_GEN_MR_AFTER reGenMrAfter,
  CASE
    WHEN (CP.RE_GEN_MR_AFTER is null OR CP.RE_GEN_MR_AFTER = 0)
    THEN null
    ELSE CP.RE_GEN_MR_AFTER
  END reGenMrAfterStr,
  CP.GEN_MR_CR_WO_BY genMrCrWoBy,
  CP.EXP_DATE expDate,
  CP.STATUS status,
  CASE
    WHEN CP.STATUS = 'I'
    THEN :inActive
    ELSE :active
  END statusName,
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
  lr.displayStr mrWorksName,
  CP.ARRAY_CHILD arrayChild,
  TO_CHAR(CP.EXP_DATE,'dd/MM/yyyy HH:mm:ss')expDateStr
FROM OPEN_PM.MR_CFG_PROCEDURE_IT_HARD CP
LEFT JOIN common_gnoc.cat_location cl
ON cl.location_id = CP.MARKET_CODE
LEFT JOIN lst_lang ll
ON ll.ITEM_CODE   = CP.ARRAY_CODE
left join list_result lr on lr.PROCEDURE_ID = CP.PROCEDURE_ID
WHERE 1           = 1 and CP.MR_MODE = 'H'
