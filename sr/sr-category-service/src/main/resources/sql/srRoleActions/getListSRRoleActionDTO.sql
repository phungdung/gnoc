SELECT A.ROLE_ACTION_ID roleActionId ,
  A.ROLE_TYPE roleType ,
  A.CURRENT_STATUS currentStatus ,
  A.NEXT_STATUS nextStatus ,
  A.ACTIONS actions ,
  A.CREATED_USER createdUser ,
  A.CREATED_TIME createdTime ,
  A.UPDATED_USER updatedUser ,
  A.UPDATED_TIME updatedTime ,
  B.COUNTRY country ,
  A.FLOW_ID flowId ,
  A.GROUP_ROLE groupRole
FROM OPEN_PM.SR_ROLE_ACTIONS A
INNER JOIN OPEN_PM.SR_FLOW_EXECUTE B
ON A.FLOW_ID   = B.FLOW_ID
AND A.FLOW_ID IS NOT NULL
WHERE 1        =1
