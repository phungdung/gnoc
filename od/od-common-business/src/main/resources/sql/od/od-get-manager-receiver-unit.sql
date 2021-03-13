select usr.USER_ID userId, usr.USERNAME username, usr.FULLNAME fullname, usr.UNIT_ID unitId, usr.EMAIL email,
usr.USER_LANGUAGE userLanguage, usr.USER_TIME_ZONE userTimeZone
from COMMON_GNOC.USERS usr
JOIN COMMON_GNOC.V_USER_ROLE usrl
ON usr.USER_ID = usrl.USER_ID
where usrl.unit_id = :unit_id and ROLE_CODE = 'TP' and usr.IS_ENABLE = 1
