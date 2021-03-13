WITH LST_RECEIVE AS
  (SELECT i.ITEM_ID itemIdReceive,
    i.ITEM_CODE itemCodeReceive,
    i.ITEM_NAME itemNameReceive
  FROM COMMON_GNOC.CAT_ITEM i
  WHERE i.STATUS        = 1
  AND i.PARENT_ITEM_ID IS NULL
  AND i.CATEGORY_ID    IN
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='RECEIVE_CALL_IPCC'
    AND status         = 1
    )
  ),
  LST_LEVEL AS
  (SELECT i.ITEM_ID itemIdLevel,
    i.ITEM_CODE itemCodeLevel,
    i.ITEM_NAME itemNameLevel
  FROM COMMON_GNOC.CAT_ITEM i
  WHERE i.STATUS        = 1
  AND i.PARENT_ITEM_ID IS NULL
  AND i.CATEGORY_ID    IN
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE='LEVEL_CALL_IPCC'
    AND status         = 1
    )
  )
SELECT a.ID id,
  a.CFG_ID cfgId,
  b.itemNameReceive cfgName,
  a.LEVEL_CALL levelCall,
  c.itemNameLevel levelCallName,
  a.TIME_PROCESS timeProcess,
  a.FILE_NAME fileName,
  a.receive_user_name receiveUserName
FROM COMMON_GNOC.CFG_TROUBLE_CALL_IPCC a
LEFT JOIN LST_RECEIVE b
ON a.CFG_ID = b.itemIdReceive
LEFT JOIN LST_LEVEL c
ON a.LEVEL_CALL = c.itemIdLevel where 1=1
