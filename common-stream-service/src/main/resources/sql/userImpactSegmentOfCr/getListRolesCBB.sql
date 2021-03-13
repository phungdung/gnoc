select a.role_id roleId,a.DESCRIPTION description, a.ROLE_CODE roleCode,
case
      when LE.LEE_VALUE is null
      then a.role_name
      else TO_CHAR(LE.LEE_VALUE) end roleName
from common_gnoc.roles a
Left join COMMON_GNOC.LANGUAGE_EXCHANGE LE
on a.ROLE_ID = LE.BUSSINESS_ID and LE.APPLIED_SYSTEM = 1 and LE.APPLIED_BUSSINESS = 1 and LE.LEE_LOCALE = :p_leeLocale
order by A.ROLE_ID desc
