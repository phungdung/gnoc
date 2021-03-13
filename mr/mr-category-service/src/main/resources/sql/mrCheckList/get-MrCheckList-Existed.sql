SELECT T1.CHECKLIST_ID checklistId ,
  T1.MARKET_CODE marketCode ,
  T1.ARRAY_CODE arrayCode ,
  T1.NETWORK_TYPE networkType ,
  T1.DEVICE_TYPE deviceType ,
  T1.DEVICE_TYPE_ALL deviceTypeAll ,
  T1.CONTENT content ,
  T1.CREATED_USER createdUser ,
  TO_CHAR(T1.CREATED_TIME, 'dd/MM/yyyy') createdTime ,
  T1.UPDATED_USER updatedUser ,
  T1.UPDATED_TIME updatedTime ,
  T1.CYCLE cycle ,
  T1.TARGET target
FROM MR_CHECKLIST T1
WHERE 1=1
