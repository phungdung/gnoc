SELECT a.cr_process_id crProcessId, 
a.cr_process_code crProcessCode, 
a.cr_process_name crProcessName, 
a.IMPACT_SEGMENT_ID impactSegmentId , 
b.LEE_VALUE description, a.PARENT_ID parentId  
FROM open_pm.cr_process a 
LEFT JOIN (select LEE_VALUE,BUSSINESS_ID  from COMMON_GNOC.LANGUAGE_EXCHANGE where APPLIED_SYSTEM  = 2 AND APPLIED_BUSSINESS = 7 and LEE_LOCALE = 'en_US') b 
ON a.CR_PROCESS_ID    = b.BUSSINESS_ID 
WHERE a.IS_ACTIVE =  1
