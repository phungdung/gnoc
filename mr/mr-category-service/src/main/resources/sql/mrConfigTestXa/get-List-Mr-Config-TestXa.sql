WITH tmp_country AS
  (SELECT LOCATION_ID,
    LOCATION_NAME
  FROM COMMON_GNOC.CAT_LOCATION
  WHERE status                   = 1
  AND level                      = 1
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_province AS
  (SELECT LOCATION_ID,
    LOCATION_NAME,
    PARENT_ID
  FROM COMMON_GNOC.CAT_LOCATION
  WHERE status                   = 1
  AND level                      = 3
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  ),
  tmp_district AS
  (SELECT LOCATION_ID,
    LOCATION_NAME
  FROM COMMON_GNOC.CAT_LOCATION
  WHERE status                   = 1
  AND level                      = 4
    START WITH parent_id        IS NULL
    CONNECT BY prior location_id = parent_id
  )
SELECT M.CONFIG_ID configId,
  tmp_country.LOCATION_NAME country,
  tmp_province.LOCATION_NAME province,
  M.TIME_TESTXA timeTestXa,
  M.STATION_AT_A_TIME stationAtATime,
  (SELECT LISTAGG(LOCATION_NAME,',') WITHIN GROUP(
  ORDER BY LOCATION_NAME )
  FROM COMMON_GNOC.CAT_LOCATION
  WHERE ','
    || M.EXCEP_DISTRICT
    || ',' LIKE '%,'
    || location_id
    || ',%'
  ) excepDistrict,
  M.EXCEP_STATION excepStation,
  M.STATUS status,
  TO_CHAR(M.CREATE_TIME,'dd/MM/yyyy HH24:MI:ss') createTime,
  M.CREATE_USER createUser,
  TO_CHAR(M.UPDATE_TIME,'dd/MM/yyyy HH24:MI:ss') updateTime,
  M.UPDATE_USER updateUser
FROM MR_CONFIG_TESTXA M
LEFT JOIN tmp_country
ON tmp_country.LOCATION_ID = M.COUNTRY
LEFT JOIN tmp_province
ON tmp_province.LOCATION_ID = M.PROVINCE
LEFT JOIN tmp_district
ON tmp_district.LOCATION_ID LIKE M.EXCEP_DISTRICT
WHERE 1 = 1
