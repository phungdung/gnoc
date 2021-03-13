SELECT  d.DEVICE_TYPE_VERSION_ID deviceTypeVersionId,
        d.VENDOR_ID vendorId,
        d.TYPE_ID typeId,
        d.SOFTWARE_VERSION softwareVersion,
        d.HARDWARE_VERSION hardwareVersion,
        d.TEMP temp,
        e.PARENT_ITEM_ID subTypeId,
        case when (k.LEE_VALUE IS NULL) THEN f.ITEM_NAME
        ELSE k.LEE_VALUE
        END
        typeIdStr,
        case when (l.LEE_VALUE IS NULL) THEN g.ITEM_NAME
        ELSE l.LEE_VALUE
        END
        vendorIdStr,
        case when (m.LEE_VALUE IS NULL) THEN n.ITEM_NAME
        ELSE m.LEE_VALUE
        END
        subTypeIdStr

FROM    COMMON_GNOC.DEVICE_TYPE_VERSION d,
        COMMON_GNOC.CAT_ITEM e,
        COMMON_GNOC.CAT_ITEM f,
        COMMON_GNOC.CAT_ITEM g,
        (select * from
        COMMON_GNOC.LANGUAGE_EXCHANGE
        WHERE APPLIED_SYSTEM = 1 and APPLIED_BUSSINESS = 3 and LEE_LOCALE = :locale) k,
        (select * from COMMON_GNOC.LANGUAGE_EXCHANGE
        WHERE APPLIED_SYSTEM = 1 and APPLIED_BUSSINESS = 3 and LEE_LOCALE = :locale) l,
        (select * from COMMON_GNOC.LANGUAGE_EXCHANGE
        WHERE APPLIED_SYSTEM = 1 and APPLIED_BUSSINESS = 3 and LEE_LOCALE = :locale) m,
        COMMON_GNOC.CAT_ITEM n
        WHERE 1=1
        AND d.TYPE_ID = e.ITEM_ID(+)
        AND d.TYPE_ID = f.ITEM_ID(+)
        AND d.VENDOR_ID = g.ITEM_ID(+)
        AND d.TYPE_ID = k.BUSSINESS_ID(+)
        AND d.VENDOR_ID = l.BUSSINESS_ID(+)
        AND e.PARENT_ITEM_ID = m.BUSSINESS_ID(+)
        AND e.PARENT_ITEM_ID = n.ITEM_ID(+)
