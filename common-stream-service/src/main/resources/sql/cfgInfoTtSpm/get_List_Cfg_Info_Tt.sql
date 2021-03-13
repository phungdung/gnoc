WITH list_language_exchange AS
  (SELECT ci.ITEM_ID,
    ci.ITEM_NAME,
    ci.ITEM_CODE,
    le.LEE_VALUE,
    le.LEE_LOCALE
  FROM COMMON_GNOC.CAT_ITEM ci
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le
  ON ci.ITEM_ID            = le.BUSSINESS_ID
  WHERE le.APPLIED_SYSTEM  = '1'
  AND le.APPLIED_BUSSINESS = '3'
  AND le.LEE_LOCALE        = :leeLocale
  ),
  listNeed AS
  (SELECT a.ID id,
    a.TYPE_ID typeId,
    a.ALARM_GROUP_ID alarmGroupId,
    a.SUB_CATEGORY_ID subCategoryId,
    a.TROUBLE_NAME troubleName,
    b.ITEM_NAME typeName,
    c.ITEM_NAME alarmGroupName,
    d.ITEM_NAME subCategoryName
  FROM CFG_INFO_TT_SPM a
  LEFT JOIN CAT_ITEM b
  ON a.TYPE_ID = b.ITEM_ID
  LEFT JOIN CAT_ITEM c
  ON a.ALARM_GROUP_ID = c.ITEM_ID
  LEFT JOIN CAT_ITEM d
  ON a.SUB_CATEGORY_ID = d.ITEM_ID
  ),
  list_result AS
  (SELECT listNeed.id,
    listNeed.typeId,
    listNeed.troubleName,
    listNeed.alarmGroupId,
    listNeed.subCategoryId,
    CASE
      WHEN llx.LEE_VALUE IS NULL
      THEN listNeed.typeName
      ELSE llx.LEE_VALUE
    END typeName,
    CASE
      WHEN llx.LEE_VALUE IS NULL
      THEN listNeed.alarmGroupName
      ELSE llx2.LEE_VALUE
    END alarmGroupName,
    CASE
      WHEN llx.LEE_VALUE IS NULL
      THEN listNeed.subCategoryName
      ELSE llx3.LEE_VALUE
    END subCategoryName
  FROM listNeed
  LEFT JOIN list_language_exchange llx
  ON listNeed.typeId = llx.ITEM_ID
  LEFT JOIN list_language_exchange llx2
  ON listNeed.alarmGroupId = llx2.ITEM_ID
  LEFT JOIN list_language_exchange llx3
  ON listNeed.subCategoryId = llx3.ITEM_ID
  )
SELECT list_result.id id,
  list_result.troubleName troubleName,
  list_result.typeId typeId,
  list_result.typeName typeName,
  list_result.alarmGroupId alarmGroupId,
  list_result.alarmGroupName alarmGroupName,
  list_result.subCategoryId subCategoryId,
  list_result.subCategoryName subCategoryName
FROM list_result
WHERE 1 = 1
