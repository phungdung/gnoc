SELECT
    T1.CFG_PROCEDURE_BTS_ID cfgProcedureBtsId
    , T1.MARKET_CODE marketCode
    , T1.DEVICE_TYPE deviceType
    , T1.CYCLE cycle
    , T1.GEN_MR_BEFORE genMrBefore
    , T1.MR_TIME mrTime
    , T1.MATERIAL_TYPE materialType
    , CASE
        WHEN T1.MATERIAL_TYPE = 'D' THEN '$fuel.type.oil$'
        WHEN T1.MATERIAL_TYPE = 'X' THEN '$fuel.type.gas$'
        ELSE T1.MATERIAL_TYPE END
      materialTypeName
    , T1.MAINTENANCE_HOUR maintenanceHour
    , T1.SUPPLIER_CODE supplierCode
    ,cl.LOCATION_NAME marketName
FROM MR_CFG_PROCEDURE_BTS T1
LEFT JOIN COMMON_GNOC.CAT_LOCATION cl ON (T1.MARKET_CODE  = cl.LOCATION_ID and cl.PARENT_ID is null and cl.status = 1)
WHERE 1=1
