WITH list_language AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  WHERE LE.APPLIED_SYSTEM  = 1
  AND LE.APPLIED_BUSSINESS = 3
  AND LE.LEE_LOCALE        = :p_leeLocale
  ),
  list_ArrayIncident AS
  (SELECT i.item_id itemId,
    i.item_code itemCode,
    CASE
      WHEN llec.LEE_VALUE IS NULL
      THEN i.item_name
      ELSE llec.LEE_VALUE
    END itemName,
    i.item_value itemValue
  FROM common_gnoc.cat_item i
  LEFT JOIN common_gnoc.category c
  ON i.category_id = c.category_id
  LEFT JOIN list_language llec ON i.item_id            = llec.BUSSINESS_ID
  WHERE i.parent_item_id IS NULL
  AND c.category_code     ='PT_TYPE'
  ORDER BY i.item_name ASC
  ),
  list_AlarmGroup AS
  (SELECT i.ITEM_ID alarmGroupId,
    i.ITEM_CODE alarmGroupCode,
     CASE
      WHEN la.LEE_VALUE IS NULL
      THEN i.ITEM_NAME
      ELSE la.LEE_VALUE
    END alarmGroupName
  FROM COMMON_GNOC.CAT_ITEM i
  left join list_language la on la.BUSSINESS_ID = i.ITEM_ID
  WHERE STATUS    = 1
  AND CATEGORY_ID =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='ALARM_GROUP'
    AND STATUS         = 1
    )
  AND PARENT_ITEM_ID IN
    (SELECT itemId FROM list_ArrayIncident
    )
  ORDER BY position,
    NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese')
  )
SELECT a.ID id,
  a.TYPE_ID typeId,
  b.itemName typeName,
  a.ALARM_GROUP_ID alarmGroupId,
  c.alarmGroupName alarmGroupName,
  a.LAST_UPDATE_TIME lastUpdateTime
FROM COMMON_GNOC.CFG_CREATE_WO a
LEFT JOIN list_ArrayIncident b
ON a.TYPE_ID = b.itemId
LEFT JOIN list_AlarmGroup c
ON a.ALARM_GROUP_ID = c.alarmGroupId
WHERE 1             =1
