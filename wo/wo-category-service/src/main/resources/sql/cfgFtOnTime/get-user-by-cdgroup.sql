select us.username username, us.fullname fullname, us.user_id userId, 
   us.unit_id unitId, ut.unit_name unitName, 
   us.mobile, us.email email 
   from common_gnoc.users us
  left join common_gnoc.unit ut on us.unit_id = ut.unit_id
  join (SELECT distinct u.UNIT_ID unitId
  FROM    WO_CD_GROUP_UNIT u
  WHERE   1 = 1
  and u.CD_GROUP_ID = :cdGroupId
  ) b
  ON us.unit_id = b.unitId
  where us.is_enable=1
