WITH list_language_exchange AS
  (SELECT cat.ITEM_VALUE,
    cat.ITEM_ID,
    cat.ITEM_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE,
    LE.LEE_LOCALE
    FROM (SELECT *
          FROM COMMON_GNOC.CAT_ITEM
          WHERE CATEGORY_ID =(
            SELECT CATEGORY_ID
            FROM COMMON_GNOC.CATEGORY
            WHERE CATEGORY_CODE= 'WO_GROUP_TYPE'
            AND EDITABLE       = 1)) cat
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
  ON LE.BUSSINESS_ID       = cat.ITEM_ID
  AND LE.APPLIED_SYSTEM    = 1
  AND LE.APPLIED_BUSSINESS = 3
  AND LE.LEE_LOCALE        = :p_leeLocale
  )
select
  w.wo_type_id  woTypeId
, w.wo_type_code woTypeCode
, CASE
    WHEN lex.LEE_VALUE IS NULL
    THEN w.wo_type_name
    ELSE lex.LEE_VALUE
  END woTypeName
, w.is_enable isEnable
, w.wo_group_type woGroupType
, w.enable_Create enableCreate
, w.TIME_OVER timeOver
, w.SMS_CYCLE smsCycle
, w.WO_CLOSE_AUTOMATIC_TIME woCloseAutomaticTime
, w.allow_pending allowPending
, w.create_from_other_sys createFromOtherSys
, w.TIME_AUTO_CLOSE_WHEN_OVER timeAutoCloseWhenOver
,CASE
    WHEN llex.LEE_VALUE IS NULL
    THEN llex.ITEM_NAME
    ELSE llex.LEE_VALUE
  END woGroupTypeName
from
	wfm.wo_type w
  LEFT JOIN list_language_exchange llex ON w.WO_GROUP_TYPE = llex.ITEM_ID
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE lex ON lex.BUSSINESS_ID =  w.wo_type_id and lex.LEE_LOCALE = :p_leeLocale and lex.APPLIED_SYSTEM = 4 and lex.APPLIED_BUSSINESS = 1
where
1=1
