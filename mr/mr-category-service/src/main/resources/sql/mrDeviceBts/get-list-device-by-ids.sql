select DEVICE_ID deviceId, MARKET_CODE marketCode, PROVINCE_CODE provinceCode, DEVICE_TYPE deviceType,
       UPDATE_USER updateUser, FUEL_TYPE fuelType, AREA_CODE areaCode,
       OPERATION_HOUR_LAST_UPDATE operationHourLastUpdate, PUT_STATUS putStatus, IN_KTTS inKTTS
From MR_DEVICE_BTS where DEVICE_ID in(:lstId)
