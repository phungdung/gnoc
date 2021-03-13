select oldStatus.item_name oldStatusName, newStatus.item_name newStatusName, oh.USER_Name userName, oh.UPDATE_TIME updateTime, oh.content
from OD_HISTORY oh
LEFT JOIN
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odStatusCode AND cat_item.STATUS = 1) oldStatus
 ON oh.OLD_STATUS = oldStatus.item_value
 LEFT JOIN
 (select cat_item.* from COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item
  where cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = :odStatusCode AND cat_item.STATUS = 1) newStatus
 ON oh.NEW_STATUS = newStatus.item_value
 where oh.OD_ID = :odId
 ORDER BY oh.OD_HIS_ID DESC
