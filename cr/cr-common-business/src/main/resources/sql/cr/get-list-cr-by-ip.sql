WITH t1 AS
  (SELECT *
  FROM open_pm.cr t1
  WHERE t1.CREATED_DATE >= sysdate -180
  AND t1.CREATED_DATE   <= sysdate
  AND t1.STATE NOT      IN (0,8)
  ),
  t2 AS
  (SELECT *
  FROM OPEN_PM.CR_IMPACTED_NODES t2
  WHERE t2.ip         = :ip
  AND t2.INSERT_TIME >= sysdate - 180
  )
SELECT t1.CR_NUMBER as crNumber,
  t1.TITLE as title,
  t3.CR_PROCESS_NAME as crProcessName,
  CASE
    WHEN t1.state = 0 THEN :duThao
    WHEN t1.state = 1 THEN :choPheDuyet
    WHEN t1.state = 2 THEN :choKiemTraDauVao
    WHEN t1.state = 3 THEN :choThamDinh
    WHEN t1.state = 4 THEN :choSapLich
    WHEN t1.state = 5 THEN :choTiepNhan
    WHEN t1.state = 6 THEN :daTiepNhan
    WHEN t1.state = 7 THEN :hoanThanh
    WHEN t1.state = 8 THEN :thieuThongTin
    WHEN t1.state = 9 THEN :dong
    WHEN t1.state = 10 THEN :thieuThongTin_Huy
    WHEN t1.state = 11 THEN :choGiaoCab
    WHEN t1.state = 12 THEN :dangCab
    ELSE ''
  END kq_cr,
  t5.USERNAME as changeResponsibleName -- nhân sự tác động  -- check lại xem đúng ko
  ,
  t6.UNIT_NAME as changeResponsibleUnitName --check laik
  ,
  t4.USERNAME as userCab --user cab
  ,
  t1.DISTURBANCE_START_TIME as disturbanceStartTime,
  t1.DISTURBANCE_END_TIME as disturbanceEndTime ,
  t8.CHANGE_DATE as changeDate,
  t7.location_name as country
FROM t1
JOIN t2
ON t1.cr_id = t2.cr_id
LEFT JOIN OPEN_PM.CR_PROCESS t3
ON t1.PROCESS_TYPE_ID = t3.CR_PROCESS_ID
LEFT JOIN COMMON_GNOC.users t4
ON t1.USER_CAB = t4.USER_ID
LEFT JOIN COMMON_GNOC.users t5
ON t1.CHANGE_RESPONSIBLE = t5.USER_ID
LEFT JOIN COMMON_GNOC.unit t6
ON t1.CHANGE_RESPONSIBLE_UNIT = t6.UNIT_ID
LEFT JOIN COMMON_GNOC.cat_location t7
ON t1.country = t7.location_id
LEFT JOIN OPEN_PM.cr_his t8
ON t1.CR_ID   = t8.CR_ID
AND t8.STATUS = 9
ORDER BY t1.CREATED_DATE DESC
