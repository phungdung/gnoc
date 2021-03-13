WITH get_unit_name AS
  (SELECT ut.UNIT_ID,
    ut.UNIT_NAME,
    vu.unit_name_full
  FROM common_gnoc.unit ut
  LEFT JOIN common_gnoc.v_unit_full vu
  ON ut.UNIT_ID = vu.UNIT_ID
  )
SELECT us.USER_ID userId ,
  us.userName username ,
  us.FULLNAME fullname ,
  us.BIRTH_DAY birthDay ,
  us.UNIT_ID unitId ,
  us.UNIT_ID_NEW unitIdNew ,
  us.DELETE_GROUP deleteGroup ,
  us.email email ,
  us.is_enable isEnable ,
  us.mobile mobile ,
  us.USER_TIME_ZONE userTimeZone ,
  us.NATION_CODE nationCode ,
  us.RELATE_UNITS relateUnits ,
  us.STAFF_CODE staffCode ,
  us.IS_NOT_RECEIVE_MESSAGE isNotReceiveMessage ,
  us.UPDATE_TIME_UNIT updateTimeUnit,
  us.HR_UNIT_ID hrUnitId,
  us.APPROVE_STATUS approveStatus,
  us.APPROVE_USER approveUser,
  us.RESOURCE_CODE resourceCode,
  us.ACTION_LOG actionLog,
  CASE
    WHEN utApp1.unit_name_full IS NULL
    THEN utApp1.unit_name
    ELSE substr(utApp1.unit_name_full, instr(utApp1.unit_name_full, '/', -1, 3)+1, Length(utApp1.unit_name_full))
  END AS unitName ,
  CASE
    WHEN utApp2.unit_name_full IS NULL
    THEN utApp2.unit_name
    ELSE substr(utApp2.unit_name_full, instr(utApp2.unit_name_full, '/', -1, 3)+1, Length(utApp2.unit_name_full))
  END AS unitNameNew ,
  CASE
    WHEN utApp3.unit_name_full IS NULL
    THEN utApp3.unit_name
    ELSE substr(utApp3.unit_name_full, instr(utApp3.unit_name_full, '/', -1, 3)+1, Length(utApp3.unit_name_full))
  END AS relateUnitsName ,
  CASE
    WHEN utApp4.unit_name_full IS NULL
    THEN utApp4.unit_name
    ELSE substr(utApp4.unit_name_full, instr(utApp4.unit_name_full, '/', -1, 3)+1, Length(utApp4.unit_name_full))
  END AS hrUnitName ,
  CASE
    WHEN us.userName IS NULL
    THEN ''
    ELSE TO_CHAR(us.userName
      || ' ('
      || us.FULLNAME
      || ')')
  END AS usernameShow
FROM COMMON_GNOC.USERS us
LEFT JOIN get_unit_name utApp1
ON us.UNIT_ID = utApp1.unit_id
LEFT JOIN get_unit_name utApp2
ON us.UNIT_ID_NEW = utApp2.unit_id
LEFT JOIN get_unit_name utApp3
ON us.RELATE_UNITS = utApp3.unit_id
LEFT JOIN get_unit_name utApp4
ON us.HR_UNIT_ID = utApp4.unit_id
WHERE 1          = 1
