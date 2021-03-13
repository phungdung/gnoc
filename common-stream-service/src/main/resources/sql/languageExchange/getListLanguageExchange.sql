select LE.LEE_ID leeId,
    LE.APPLIED_SYSTEM appliedSystem,
    LE.APPLIED_BUSSINESS appliedBussiness,
    LE.BUSSINESS_ID bussinessId,
    LE.BUSSINESS_CODE bussinessCode,
    LE.LEE_LOCALE leeLocale,
    LE.LEE_VALUE leeValue
from COMMON_GNOC.LANGUAGE_EXCHANGE LE
where  1=1
