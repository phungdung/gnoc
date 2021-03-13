SELECT ocs.SEND_CREATE sendCreate, ocs.CREATE_CONTENT createContent, ocs.SEND_RECEIVE_USER sendReceiveUser,
 ocs.RECEIVE_USER_CONTENT receiveUserContent, ocs.IS_DEFAULT isDefault, ocs.SEND_RECEIVE_UNIT sendReceiveUnit, ocs.RECEIVE_UNIT_CONTENT receiveUnitContent, ocs.NEXT_ACTION nextAction, ot.OD_TYPE_NAME odTypeName,od_pri.item_name odPriorityName,oldStatus.item_name oldStatusName,
 newStatus.item_name newStatusName, bus.IS_REQUIRED isRequired, bus.COLUMN_NAME columnName, col.ITEM_NAME columnNameValue, bus.EDITABLE editable, bus.IS_VISIBLE isVisible, bus.MESSAGE, ot.OD_TYPE_CODE odTypeCode
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
 LEFT JOIN wfm.OD_CFG_BUSINESS bus
 ON ocs.ID = bus.OD_CHANGE_STATUS_ID
 LEFT JOIN
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odColumnCode AND cat_item.STATUS = 1) col
 ON bus.column_name = col.item_value
 WHERE 1 = 1
