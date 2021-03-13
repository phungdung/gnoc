SELECT
  m.DATE_TIME DATETIME,
  m.DEVICE_CODE deviceCode,
  m.DEVICE_TYPE deviceType,
  m.FUEL_TYPE fuelType,
  m.GENERIC_CODE genericCode,
  m.MATERIAL_CODE materialCode,
  m.MATERIAL_ID materialId,
  m.MATERIAL_NAME materialName,
  m.POWER power,
  m.PROVINCE_CODE provinceCode,
  m.QUANTITY quantity,
  m.QUOCTA quocta,
  m.SERIAL serial,
  m.STATION_CODE stationCode,
  m.UNIT unit,
  m.UNIT_PRICE unitPrice,
  m.WO_ID woId
FROM MR_MATERIAL_DISPLACEMENT m
WHERE 1=1
