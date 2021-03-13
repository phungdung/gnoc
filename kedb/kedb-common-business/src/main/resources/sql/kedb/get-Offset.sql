select      gte.timezone_offset offset
from        Common_gnoc.USERS us
left join   Common_gnoc.gnoc_timezone gte on us.USER_TIME_ZONE = gte.GNOC_TIMEZONE_ID
WHERE       lower(us.username) = :userName
