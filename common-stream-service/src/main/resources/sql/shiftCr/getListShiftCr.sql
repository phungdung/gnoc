SELECT
t1.ID id,
t1.CR_NUMBER crNumber,
t1.CR_NAME crName,
t1.PURPOSE purpose,
t1.UNIT_NAME unitName,
t1.USER_NAME userName,
t1.USER_CHECK_NAME userCheckName,
t1.RESULT result,
t1.NOTE note,
t1.SHIFT_HANDOVER_ID shiftHandoverId,
t1.COUNTRY country
FROM COMMON_GNOC.SHIFT_CR t1
WHERE 1=1
