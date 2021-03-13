select c.ID,c.CD_ID cdId,g.WO_GROUP_NAME cdGroupName, g.WO_GROUP_CODE cdGroupCode
 ,c.CFG_TIME cfgTime,c.USER_ID userId,u.USERNAME
 ,c.BUSINESS_ID businessId,i.ITEM_NAME businessName
 ,to_char(c.CFG_TIME,'dd/MM/yyyy') cfgTimeStr
 from wfm.CFG_FT_ON_TIME c
 left join COMMON_GNOC.users u on c.USER_ID = u.USER_ID
 left join COMMON_GNOC.CAT_ITEM i on c.BUSINESS_ID = i.ITEM_ID
 left join wfm.WO_CD_GROUP g on c.CD_ID = g.WO_GROUP_ID
 where 1=1
