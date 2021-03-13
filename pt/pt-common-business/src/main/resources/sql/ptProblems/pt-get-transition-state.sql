SELECT
 cat_item.item_id itemId, cat_item.item_name itemName, item_code itemCode, tsc.role_code roleCode
FROM COMMON_GNOC.CATEGORY cat,COMMON_GNOC.CAT_ITEM cat_item, COMMON_GNOC.TRANSITION_STATE_CONFIG tsc
WHERE
cat.CATEGORY_ID = cat_item.CATEGORY_ID AND cat.category_code = 'PT_STATE' AND cat_item.STATUS = 1
AND cat_item.ITEM_ID = tsc.END_STATE_ID
