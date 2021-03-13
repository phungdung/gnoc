  SELECT   a.item_id itemId,
  a.item_code itemCode,
  a.item_name itemName,
  a.item_value itemValue
  FROM   COMMON_GNOC.cat_item a, ONE_TM.CFG_TIME_TROUBLE_PROCESS b
  WHERE   a.item_id = b.PRIORITY_ID
