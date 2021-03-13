SELECT  m.CHECKLIST_ID checklistId,
        m.MARKET_CODE marketCode,
        m.ARRAY_CODE arrayCode,
        m.DEVICE_TYPE deviceType,
        m.MATERIAL_TYPE materialType,
        m.CREATED_USER createdUser,
        m.CREATED_TIME createdTime,
        m.UPDATED_USER updatedUser,
        m.UPDATED_TIME updatedTime,
        m.CYCLE cycle,
        m.SUPPLIER_CODE supplierCode,
        m.IMES_CHECK imesCheck
FROM    MR_CHECKLISTS_BTS m
WHERE   1 = 1
