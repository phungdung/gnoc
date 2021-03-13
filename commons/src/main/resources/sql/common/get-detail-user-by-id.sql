SELECT
us.USER_ID userId,
us.userName username,
us.FULLNAME fullname,
us.BIRTH_DAY birthDay,
us.USER_LANGUAGE userLanguage,
us.UNIT_ID unitId,
us.email email,
us.is_enable isEnable,
us.mobile mobile,
us.IS_NOT_RECEIVE_MESSAGE isNotReceiveMessage,
us.STAFF_CODE staffCode,
us.UNIT_ID_NEW unitIdNew,
us.DELETE_GROUP deleteGroup,
ut.unit_name unitName,
(select LISTAGG(IMPACT_SEGMENT_ID,',') WITHIN GROUP (ORDER BY IMPACT_SEGMENT_ID)
from COMMON_GNOC.USER_IMPACT_SEGMENT where USER_ID = us.USER_ID) lstImpact,
(select LISTAGG(ROLE_ID,',') WITHIN GROUP (ORDER BY ROLE_ID) FROM COMMON_GNOC.ROLE_USER
where USER_ID = us.USER_ID) lstRole,
(select SMS_TYPE from ONE_TM.USER_SMS where USER_ID = us.USER_ID) message,
(select TYPE_CODE from ONE_TM.USER_SMS where USER_ID = us.USER_ID) typeCode

FROM COMMON_GNOC.USERS us
LEFT JOIN common_gnoc.unit ut
 on us.unit_id = ut.unit_id
where user_id = :userId
