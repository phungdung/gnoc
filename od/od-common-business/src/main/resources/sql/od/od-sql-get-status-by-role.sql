 select distinct oc.NEW_STATUS itemValue, sts.ITEM_NAME itemName from od od, OD_CHANGE_STATUS oc, OD_CHANGE_STATUS_ROLE ocr,
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odStatusCode AND cat_item.STATUS = 1) sts
 WHERE 1 = 1
 AND od.STATUS = oc.OLD_STATUS
 AND od.PRIORITY_ID = oc.OD_PRIORITY
 AND oc.Id = ocr.OD_CHANGE_STATUS_ID
 AND oc.NEW_STATUS = sts.ITEM_VALUE(+)
 AND od.OD_ID = :odId
