SELECT cd.DEVICE_CD_ID deviceCdId,
       cd.DEVICE_TYPE deviceType,
       cd.DEVICE_NAME deviceName,
       cd.STATION_CODE stationCode,
       cd.USER_MR_HARD userMrHard,
       cd.STATUS,
       cd.UPDATE_DATE updateDate,
       cd.UPDATE_USER updateUser,
       cd.MARKET_CODE marketCode,
       cl.location_name marketName
FROM MR_DEVICE_CD cd
LEFT JOIN COMMON_GNOC.CAT_LOCATION cl ON (cd.MARKET_CODE = cl.LOCATION_ID and cl.PARENT_ID is null and cl.status = 1)
WHERE 1 = 1
AND NVL(cd.IS_DELETE, 0) = 0
