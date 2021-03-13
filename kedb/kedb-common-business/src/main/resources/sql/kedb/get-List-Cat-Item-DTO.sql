SELECT  i.item_id itemId,
		    i.item_code itemCode,
        i.item_name itemName,
		    i.item_value itemValue,
		    i.category_id categoryId,
		    c.category_name categoryIdName,
		    c.category_code categoryCode,
		    c.description description,
		    i.parent_item_id parentItemId,
		    i.status status
FROM   	common_gnoc.cat_item i,
		    common_gnoc.category c
WHERE  	i.category_id = c.category_id
