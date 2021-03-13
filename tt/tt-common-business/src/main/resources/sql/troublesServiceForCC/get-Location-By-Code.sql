SELECT  a.LOCATION_ID as locationId,
        a.location_code as locationCode,
        SYS_CONNECT_BY_PATH (a.LOCATION_ID, '/')|| '/' as locationIdFull,
        SYS_CONNECT_BY_PATH (a.location_name, ' / ')|| '/' as locationNameFull,
        LEVEL AS locationLevel
FROM    common_gnoc.cat_location a
WHERE   LEVEL < 6
