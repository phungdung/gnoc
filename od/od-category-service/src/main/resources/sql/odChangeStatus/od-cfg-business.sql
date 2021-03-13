SELECT ocs.ID Id, ot.OD_TYPE_NAME odTypeName,od_pri.item_name odPriorityName,oldStatus.item_name oldStatusName,
 newStatus.item_name newStatusName, ocs.IS_DEFAULT isDefault, ocs.OD_TYPE_ID odTypeId, ocs.OLD_STATUS oldStatus, ocs.NEW_STATUS newStatus, ocs.OD_PRIORITY odPriority
 FROM wfm.OD_CHANGE_STATUS ocs
 LEFT JOIN wfm.OD_TYPE ot
 ON
 ocs.OD_TYPE_ID = ot.OD_TYPE_ID
 LEFT JOIN
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odStatusCode AND cat_item.STATUS = 1) oldStatus
 ON ocs.OLD_STATUS = oldStatus.item_value
 LEFT JOIN
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odStatusCode AND cat_item.STATUS = 1) newStatus
 ON ocs.NEW_STATUS = newStatus.item_value
 LEFT JOIN
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odPriorityCode AND cat_item.STATUS = 1) od_pri
 ON ocs.OD_PRIORITY = od_pri.item_id
 WHERE 1 = 1
