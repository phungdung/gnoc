SELECT b.ID id ,
  b.CFG_TYPE_ID cfgTypeId ,
  b.CD_ID cdId ,
  i.ITEM_NAME configType ,
  g.WO_GROUP_NAME cdName ,
  b.CFG_LEVEL cfgLevel
FROM WFM.cfg_business_call_sms b
INNER JOIN COMMON_GNOC.CAT_ITEM i
ON b.CFG_TYPE_ID = i.ITEM_ID
INNER JOIN WFM.WO_CD_GROUP g
ON b.CD_ID = g.WO_GROUP_ID
WHERE 1    = 1
