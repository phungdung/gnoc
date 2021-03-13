WITH lst_lang AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 263
    AND ITEM_CODE     = :p_system
    ) CAT1
  ON LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 262
    AND ITEM_CODE     = :p_bussiness
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  lst_status AS
  (SELECT cat.ITEM_ID,
    cat.ITEM_CODE,
    CASE
      WHEN ll.LEE_VALUE IS NOT NULL
      THEN ll.LEE_VALUE
      ELSE cat.ITEM_NAME
    END ITEM_NAME
  FROM COMMON_GNOC.CAT_ITEM cat
  LEFT JOIN lst_lang ll
  ON ll.bussiness_id      = cat.ITEM_ID
  WHERE cat.STATUS        = 1
  AND cat.PARENT_ITEM_ID IS NULL
  AND cat.CATEGORY_ID     =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE = :categoryCode
    AND status          = 1
    )
  )
SELECT ttcs.ID id,
  ttcs.TT_TYPE_ID ttTypeId,
  mang.item_name typeName,
  ttcs.ALARM_GROUP alarmGroup,
  alarm.item_name alarmGroupName,
  ttcs.OLD_STATUS oldStatus,
  ci1.ITEM_NAME oldStatusName,
  ttcs.NEW_STATUS newStatus,
  ci2.ITEM_NAME newStatusName,
  ttcs.SEND_CREATE sendCreate,
  ttcs.CREATE_CONTENT createContent,
  ttcs.SEND_RECEIVE_USER sendReceiveUser,
  ttcs.RECEIVE_USER_CONTENT receiveUserContent,
  ttcs.IS_DEFAULT isDefault,
  ttcs.SEND_CREATE_UNIT sendCreateUnit,
  ttcs.CREATE_UNIT_CONTENT createUnitContent,
  ttcs.SEND_RECEIVE_UNIT_LV1 sendReceiveUnitLv1,
  ttcs.RECEIVE_UNIT_LV1_CONTENT receiveUnitLv1Content,
  ttcs.SEND_RECEIVE_UNIT_LV2 sendReceiveUnitLv2,
  ttcs.RECEIVE_UNIT_LV2_CONTENT receiveUnitLv2Content,
  ttcs.TT_PRIORITY ttPriority,
  ttcs.NEXT_ACTION nextAction
FROM ONE_TM.TT_CHANGE_STATUS ttcs
INNER JOIN lst_status ci1
ON ttcs.OLD_STATUS = ci1.ITEM_ID
INNER JOIN lst_status ci2
ON ttcs.NEW_STATUS = ci2.ITEM_ID
LEFT JOIN common_gnoc.cat_item mang
ON ttcs.TT_TYPE_ID = mang.ITEM_ID
LEFT JOIN common_gnoc.cat_item alarm
ON TO_CHAR(ttcs.ALARM_GROUP) = TO_CHAR(alarm.ITEM_ID)
where 1=1
