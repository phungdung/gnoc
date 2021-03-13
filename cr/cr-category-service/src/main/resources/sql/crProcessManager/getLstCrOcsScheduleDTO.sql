select a.cr_ocs_schedule_id crOcsScheduleId, a.user_id userId, b.username userName,a.cr_process_id crProcessId,c.cr_process_name crProcessName ,
d.cr_process_name crProcessParentName  from cr_ocs_schedule a  inner join common_gnoc.users b on a.user_id=b.user_id  inner join cr_process
 c on a.cr_process_id =c.cr_process_id  left join cr_process d on c.parent_id = d.cr_process_id  where 1 = 1
