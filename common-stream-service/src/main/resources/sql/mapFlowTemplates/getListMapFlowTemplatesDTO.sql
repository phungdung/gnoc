SELECT a.id id,a.userID userID,to_char(a.lastUpdateTime, 'dd/MM/yyyy HH:mm:ss') lastUpdateTime,a.updateUser updateUser,a.typeId typeId,a.typeName typeName,a.alarmGroupId alarmGroupId,a.alarmGroupName alarmGroupName FROM (  WITH
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
  join (select * from COMMON_GNOC.CAT_ITEM where  ITEM_CODE = '1') CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  join (select * from COMMON_GNOC.CAT_ITEM where ITEM_CODE = '3') CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.LEE_LOCALE = :p_leeLocale
),

list_search AS(SELECT a.ID id, b.ITEM_NAME typeName,a.TYPE_ID typeId,a.ALARM_GROUP_ID alarmGroupId,c.ITEM_NAME alarmGroupName,a.LAST_UPDATE_TIME lastUpdateTime, d.FULLNAME updateUser,a.USER_ID userID
           FROM COMMON_GNOC.MAP_FLOW_TEMPLATES a LEFT JOIN COMMON_GNOC.CAT_ITEM b ON a.TYPE_ID=b.ITEM_ID
           LEFT JOIN COMMON_GNOC.CAT_ITEM c ON a.ALARM_GROUP_ID=c.ITEM_ID LEFT JOIN COMMON_GNOC.USERS d ON a.USER_ID=d.USER_ID WHERE 1=1),

list_result1 AS (
  select
   a.ID id,
   a.userID,
    a.lastUpdateTime,
    a.updateUser,a.typeId,a.alarmGroupId,a.alarmGroupName,
    case
      when llx.LEE_VALUE is null
      then  a.typeName
      else llx.LEE_VALUE end typeName
  from list_search a
  left join list_language_exchange llx on a.typeId = llx.BUSSINESS_ID
),
list_result2 AS (
  select
   b.id,b.userID ,
    b.lastUpdateTime ,
    b.updateUser ,b.typeId ,b.typeName ,b.alarmGroupId ,
    case
      when llx.LEE_VALUE is null
      then b.alarmGroupName
      else llx.LEE_VALUE end alarmGroupName
  from list_result1 b
  left join list_language_exchange llx on b.alarmGroupId = llx.BUSSINESS_ID
)
select
c.id id,c.userID userID,c.lastUpdateTime lastUpdateTime,c.updateUser updateUser,c.typeId typeId,c.typeName typeName,c.alarmGroupId alarmGroupId,c.alarmGroupName alarmGroupName
from list_result2 c) a where 1=1
