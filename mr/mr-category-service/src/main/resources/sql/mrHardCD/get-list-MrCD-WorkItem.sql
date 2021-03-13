SELECT CD.WI_ID wiId,
       CD.MARKET_CODE marketCode,
       CD.ARRAY_CODE arrayCode,
       CD.DEVICE_TYPE deviceType,
       CD.CYCLE cycle,
       CD.WORKITEMS workItems,
       CD.CREATED_DATE createdDate,
       CD.CREATED_USER createdUser,
       CD.UPDATED_DATE updatedDate,
       CD.UPDATED_USER updatedUser
FROM MR_CD_WORKITEM CD
WHERE 1 = 1
