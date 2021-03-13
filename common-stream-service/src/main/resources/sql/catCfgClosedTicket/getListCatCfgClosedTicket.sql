-- SELECT a.id id,
--   a.wo_type_id woTypeId ,
--   a.type_id typeId,
--   a.alarm_group_id alarmGroupId,
--   TO_CHAR(a.last_update_time,'dd/mm/yyyy hh:mi:ss') lastUpdateTime ,
--   ag.item_code alarmGroupCode,
--   ag.item_name alarmGroupName,
--   t.item_code typeCode,
--   t.item_name typeName ,
--   wt.wo_type_code woTypeCode,
--   wt.wo_type_name woTypeName
-- FROM common_gnoc.cat_cfg_closed_ticket a ,
--   common_gnoc.cat_item ag ,
--   common_gnoc.cat_item t ,
--   wfm.wo_type wt
-- WHERE a.wo_type_id   = wt.wo_type_id
-- AND a.type_id        = t.item_id
-- AND a.alarm_group_id = ag.item_id
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
  where LE.APPLIED_SYSTEM = 1 and LE.APPLIED_BUSSINESS = 3 and LE.LEE_LOCALE = :leeLocale
),
list_type AS (
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
  where ci.category_id = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE='PT_TYPE' and EDITABLE = 1)
),
list_alarm_group AS (
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
  where ci.category_id = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE='ALARM_GROUP' and EDITABLE = 1)
),
lang_exchange_wo_type AS (
  SELECT  t.WO_TYPE_ID,
          t.WO_TYPE_CODE,
          t.WO_TYPE_NAME,
          le2.LEE_VALUE,
          le2.LEE_LOCALE
  FROM    WFM.WO_TYPE t
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.WO_TYPE_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = 4
                                                      AND le2.APPLIED_BUSSINESS = 1
                                                      AND le2.LEE_LOCALE = :leeLocale
),
list_wo as(
select  w.wo_type_id  woTypeId,
        w.wo_type_code woTypeCode,
        case
          when
          lewt.LEE_VALUE is null
          then to_char(lewt.WO_TYPE_NAME)
          else to_char(lewt.LEE_VALUE) end woTypeName
from    wfm.wo_type w,
        lang_exchange_wo_type lewt
where   w.is_enable = 1
and     w.wo_type_id = lewt.wo_type_id
)
select
  a.id id,
  a.wo_type_id woTypeId ,
  a.type_id typeId,
  a.alarm_group_id alarmGroupId,
  TO_CHAR(a.last_update_time,'dd/mm/yyyy hh:mi:ss') lastUpdateTime ,
  lag.item_code alarmGroupCode,
  lag.item_name alarmGroupName,
  lct.item_code typeCode,
  lct.item_name typeName ,
  lw.woTypeCode ,
  lw.woTypeName
from common_gnoc.cat_cfg_closed_ticket a
left join list_type lct on lct.item_id = to_char(a.type_id)
left join list_alarm_group lag on lag.item_id = to_char(a.alarm_group_id)
left join list_wo lw on lw.woTypeId = a.wo_type_id
where 1=1
