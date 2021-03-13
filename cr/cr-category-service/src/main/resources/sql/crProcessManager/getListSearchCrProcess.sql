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
list_cr_process AS (
  select
    cr_process_id,
    cr_process_code,
    cr_process_name,
    description,
    impact_segment_id,
    device_type_id,
    subcategory_id,
    risk_level,
    impact_type,
    cr_type_id,
    is_active,
    parent_id,
    impact_characteristic,
    CONNECT_BY_ISLEAF is_leaf,
    LEVEL cr_process_level
  from open_pm.cr_process
  start with (:p_parent is null and parent_id is null) or (:p_parent is not null and cr_process_id = :p_parent)
  connect by prior cr_process_id = parent_id
),
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
  where LE.APPLIED_SYSTEM = 1 and LE.APPLIED_BUSSINESS = 3 and LE.LEE_LOCALE = :p_leeLocale
),
list_cr_type AS (
  select
  ci.item_id,
  ci.item_code,
  case
    when llec.LEE_VALUE is null
    then ci.item_name
    else llec.LEE_VALUE end item_name,
  ci.item_value
  from common_gnoc.cat_item ci
  left join list_language_exchange_cat llec on ci.item_id = llec.BUSSINESS_ID
  where ci.category_id = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE='CR_TYPE' and EDITABLE = 1)
),
list_risk_priority AS (
  select
  ci.item_id,
  ci.item_code,
  case
    when llec.LEE_VALUE is null
    then ci.item_name
    else llec.LEE_VALUE end item_name,
  ci.item_value
  from common_gnoc.cat_item ci
  left join list_language_exchange_cat llec on ci.item_id = llec.BUSSINESS_ID
  where ci.category_id = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE='RISK_PRIORITY' and EDITABLE = 1)
),
list_language_exchange_dev AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = 'OPEN_PM') CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.DEVICE_TYPES') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
list_device_type AS (
  select
  dt.device_type_id,
  dt.device_type_code,
  case
    when lled.LEE_VALUE is null
    then dt.device_type_name
    else lled.LEE_VALUE end device_type_name
  from OPEN_PM.DEVICE_TYPES dt
  left join list_language_exchange_dev lled on dt.device_type_id = lled.BUSSINESS_ID
),
list_language_exchange_imp AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = 'OPEN_PM') CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'OPEN_PM.IMPACT_SEGMENT') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),
list_impact_segment AS (
  select
  dt.impact_segment_id,
  dt.impact_segment_code,
  case
    when llei.LEE_VALUE is null
    then dt.impact_segment_name
    else llei.LEE_VALUE end impact_segment_name
  from OPEN_PM.IMPACT_SEGMENT dt
  left join list_language_exchange_imp llei on dt.impact_segment_id = llei.BUSSINESS_ID
),
list_data AS (
  select
    lcp.cr_process_id,
    lcp.cr_process_code,
    case
      when lle.LEE_VALUE is null
      then lcp.cr_process_name
      else lle.LEE_VALUE end cr_process_name,
    lcp.description,
    lcp.impact_segment_id,
    lcp.device_type_id,
    lcp.subcategory_id,
    lcp.risk_level,
    lcp.impact_type,
    lcp.cr_type_id,
    lcp.is_active,
    lcp.parent_id,
    lcp.impact_characteristic,
    lcp.is_leaf,
    lcp.cr_process_level
  from list_cr_process lcp
  left join list_language_exchange lle on lcp.cr_process_id = lle.BUSSINESS_ID
)
select
  ld.cr_process_id crProcessId,
  ld.cr_process_code crProcessCode,
  ld.cr_process_name crProcessName,
  ld.description description,
  ld.impact_segment_id impactSegmentId,
  ld.device_type_id deviceTypeId,
  ld.subcategory_id subcategoryId,
  ld.risk_level riskLevel,
  ld.impact_type impactType,
  ld.cr_type_id crTypeId,
  ld.is_active isActive,
  ld.parent_id parentId,
  ld.impact_characteristic impactCharacteristic,
  ld.is_leaf isleaf,
--   ld.cr_process_level crProcessLevel,
  lct.item_name crTypeName,
  lrp.item_name riskLevelName,
  ldt.device_type_name deviceTypeName,
  ldt.device_type_code deviceTypeCode,
  lis.impact_segment_name impactSegmentName,
  lis.impact_segment_code impactSegmentCode
from list_data ld
left join list_cr_type lct on lct.item_value = to_char(ld.cr_type_id)
left join list_risk_priority lrp on lrp.item_value = to_char(ld.risk_level)
left join list_device_type ldt on ldt.device_type_id = ld.device_type_id
left join list_impact_segment lis on lis.impact_segment_id = ld.impact_segment_id
where 1=1
