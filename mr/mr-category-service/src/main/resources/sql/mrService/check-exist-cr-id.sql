  select cr.cr_id crId,mr.mr_id mrId
  from OPEN_PM.CR_CREATED_FROM_OTHER_SYS cr
  inner join OPEN_PM.MR mr
  on cr.OBJECT_ID = mr.MR_ID
  where cr.SYSTEM_ID = 1
  and cr.cr_id > 0
  and cr.cr_id= :crId
