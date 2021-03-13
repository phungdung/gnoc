SELECT cd.DEVICE_CD_ID,
       cd.DEVICE_TYPE,
       cd.DEVICE_NAME,
       cd.STATION_CODE,
       cd.USER_MR_HARD,
       cd.STATUS,
       cd.UPDATE_DATE,
       cd.UPDATE_USER,
       cd.MARKET_CODE,
       cl.location_name marketName
FROM MR_DEVICE_CD cd, COMMON_GNOC.CAT_LOCATION cl
WHERE cd.MARKET_CODE = cl.LOCATION_ID(+)
