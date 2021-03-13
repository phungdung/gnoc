SELECT  o.CFG_ID id,
        o.OD_NAME odName,
        o.OD_DESCRIPTION odDescription,
        o.OD_TYPE_ID odTypeId,
        t.OD_TYPE_NAME odTypeName,
        t.odGroupTypeName,
        o.OD_PRIORITY odPriority,
        ci1.ITEM_NAME odPriorityName,
        o.SCHEDULE schedule,
        s.ITEM_NAME scheduleName,
        o.OD_FILE_ID odFileId,
        o.RECEIVE_UNIT receiveUnit
FROM    OD_CFG_SCHEDULE_CREATE o,
        COMMON_GNOC.CAT_ITEM ci1,
        (SELECT ot.*, ci2.ITEM_NAME odGroupTypeName FROM OD_TYPE ot LEFT JOIN COMMON_GNOC.CAT_ITEM ci2 ON ot.OD_GROUP_TYPE_ID = ci2.ITEM_ID) t,
        (SELECT ci3.* FROM COMMON_GNOC.CAT_ITEM ci3 WHERE ci3.CATEGORY_ID = (SELECT c.CATEGORY_ID FROM COMMON_GNOC.CATEGORY c WHERE c.CATEGORY_CODE = :categoryCode)) s
WHERE   o.OD_PRIORITY = ci1.ITEM_ID
  AND   to_char(o.SCHEDULE) = s.ITEM_VALUE
  AND   o.OD_TYPE_ID = t.OD_TYPE_ID
