SELECT
us.USER_ID userId
, us.userName username
, us.FULLNAME fullname
, us.BIRTH_DAY birthDay
, us.UNIT_ID unitId
, us.UNIT_ID_NEW unitIdNew
, us.HR_UNIT_ID hrUnitId
, us.UPDATE_TIME_UNIT updateTimeUnit
, us.DELETE_GROUP deleteGroup
, us.APPROVE_STATUS approveStatus
, us.APPROVE_USER approveUser
, us.EMAIL email
, us.IS_ENABLE isEnable
, us.MOBILE mobile
, us.USER_TIME_ZONE userTimeZone
, us.NATION_CODE nationCode
, us.RELATE_UNITS relateUnits
, ut3.UNIT_NAME relateUnitsName
, us.STAFF_CODE staffCode
, us.IS_NOT_RECEIVE_MESSAGE isNotReceiveMessage
, ut1.UNIT_NAME unitNameNew
, ut.UNIT_NAME unitName
, ut2.UNIT_NAME hrUnitName
, us.RESOURCE_CODE resourceCode
,CASE
    WHEN us.userName IS NULL
    THEN ''
    ELSE TO_CHAR(us.userName
      || ' ('
      || us.FULLNAME
      || ')')
  END AS usernameShow
FROM COMMON_GNOC.USERS us
LEFT JOIN common_gnoc.unit ut
 on us.UNIT_ID = ut.UNIT_ID
LEFT JOIN common_gnoc.unit ut1
 on us.UNIT_ID_NEW = ut1.UNIT_ID
LEFT JOIN common_gnoc.unit ut2
 on us.HR_UNIT_ID = ut2.UNIT_ID
LEFT JOIN common_gnoc.unit ut3
 on TO_CHAR(us.RELATE_UNITS) = TO_CHAR(ut3.UNIT_ID)
where 1 = 1
