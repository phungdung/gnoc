SELECT  d.DEVICE_TYPE_VERSION_ID deviceTypeVersionId,
        d.VENDOR_ID vendorId,
        d.TYPE_ID typeId,
        d.SOFTWARE_VERSION softwareVersion,
        d.HARDWARE_VERSION hardwareVersion,
        d.TEMP temp
FROM    COMMON_GNOC.DEVICE_TYPE_VERSION d
WHERE   1=1

