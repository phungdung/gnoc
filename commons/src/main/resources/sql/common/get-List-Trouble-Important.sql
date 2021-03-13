select
    t.TROUBLE_ID troubleId
    , t.TROUBLE_NAME troubleName
    , (select c.ITEM_NAME from COMMON_GNOC.CAT_ITEM c where c.CATEGORY_ID =
      (select CATEGORY_ID from COMMON_GNOC.category where CATEGORY_CODE = 'GNOC_COUNTRY')
       AND c.ITEM_ID = t.COUNTRY_ID) countryId
    , (select c.ITEM_NAME from COMMON_GNOC.CAT_ITEM c where c.CATEGORY_ID =
      (select CATEGORY_ID from COMMON_GNOC.category where CATEGORY_CODE = 'PRIORITY_CODE_IMPORTANT')
       AND c.ITEM_ID = t.PRIORITY_ID) priorityId
    , t.CLASS_NETWORK classNetwork
    , t.ARRAY array
    , t.IS_ALARM isAlarm
    , t.START_TIME startTime
    , t.END_TIME endTime
    , t.PROCESS_TIME processTime
    , t.NUMBER_PAKH numberPakh
    , t.NUMBER_PAKH_REAL numberPakhReal
    , t.GROUP_REASON groupReason
    , t.NATURE nature
    , t.DETAIL detail
    , t.COMFIRM_TROUBLE confirmTrouble
    , t.SERVICE_OWNER serviceOwner
    , (select c.ITEM_NAME from COMMON_GNOC.CAT_ITEM c where c.CATEGORY_ID =
      (select CATEGORY_ID from COMMON_GNOC.category where CATEGORY_CODE = 'UNIT_PROCESS_TROUBLE')
       AND c.ITEM_ID = t.UNIT_ID) unitId
    , t.PT_CODE ptCode
    , t.VENDER vender
    , t.IS_NOT_ALARM isNotAlarm
    , t.TRIGGER_UNIT triggerUnit
    , t.WEEK week
    , t.DETECT_TIME detectTime
    , t.DETECT_TROUBLE_TIME detectTroubeTime
    , (select USERNAME from COMMON_GNOC.USERS where USER_ID = t.USER_CREATE) userCreate
    , (select c.ITEM_NAME from COMMON_GNOC.CAT_ITEM c where c.CATEGORY_ID =
      (select CATEGORY_ID from COMMON_GNOC.category where CATEGORY_CODE = 'DECTECT_TROUBLE')
       AND c.ITEM_ID = t.USER_DETECT) userDetect
    , (select c.ITEM_NAME from COMMON_GNOC.CAT_ITEM c where c.CATEGORY_ID =
      (select CATEGORY_ID from COMMON_GNOC.category where CATEGORY_CODE = 'PROCESS_TROUBLE')
       AND c.ITEM_ID = t.USER_PROCESS) userProcess
    , (select USERNAME from COMMON_GNOC.USERS where USER_ID = t.COORDINATION) coordination
    , t.ARRAY_NAME arrayName
    , t.GROUP_REASON_NAME groupReasonName
from COMMON_GNOC.MANAGERMENT_TROUBLE_IMPORTANT t
where 1 = 1
