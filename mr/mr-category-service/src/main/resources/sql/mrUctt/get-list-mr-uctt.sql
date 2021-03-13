SELECT UCTT_ID ucttId ,
       CD_ID cdId ,
       c.wo_group_name cdName,
       TITLE title,
       DESCRIPTION description,
       START_DATE startDate,
       END_DATE endDate,
       CREATED_DATE createdDate,
       CREATED_USER createdUser,
       UPDATED_DATE updatedDate,
       UPDATED_USER updatedUser,
       WO_CODE woCode
 FROM OPEN_PM.MR_UCTT uc
 left join WFM.WO_CD_GROUP c on uc.CD_ID = c.WO_GROUP_ID
 WHERE 1 = 1
