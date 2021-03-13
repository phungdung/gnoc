SELECT
t1.ID id,
t1.INFO_TICKET infoTicket,
t1.DESCRIPTION description,
t1.AFFECT affect,
t1.REASON reason,
t1.CORRECTIVE_ACTION correctiveAction,
t1.NEXT_ACTION nextAction,
t1.COUNTRY country,
t1.SHIFT_HANDOVER_ID shiftHandoverId
FROM COMMON_GNOC.SHIFT_IT_SERIOUS t1
WHERE 1 = 1
