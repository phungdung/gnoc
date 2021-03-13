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
list_language_exchange_cat_rea AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = 3 and LE.APPLIED_BUSSINESS = 1 and LE.LEE_LOCALE = :leeLocale
),
list_data AS (
 select
  a.id key,
  a.id value,
  a.REASON_NAME title,
  0 disabled
FROM ONE_TM.CAT_REASON a
),
list_cat_reason AS (select
  ld.key,
  ld.value,
  case
    when llx.LEE_VALUE is null
    then ld.title
    else TO_CHAR(llx.LEE_VALUE) end title,
  ld.disabled
from list_data ld
left join list_language_exchange_cat_rea llx on ld.value = llx.BUSSINESS_ID
)
select a.id, a.TYPE_ID typeId,
                 lct.ITEM_NAME typeName,
                 a.ALARM_GROUP_ID alarmGroupId,
                 lag.ITEM_NAME alarmGroupName,
                 a.REASON_ID reasonId,
                 lw.title reasonName
                 from COMMON_GNOC.CFG_REQUIRE_HAVE_WO a
left join list_type lct on lct.item_id = to_char(a.type_id)
left join list_alarm_group lag on lag.item_id = to_char(a.alarm_group_id)
left join list_cat_reason lw on lw.key = a.REASON_ID
where 1=1
