SELECT CADT_ID cadtId,
  CR_ID crId,
  UNIT_ID unitId,
  CADT_LEVEL cadtLevel,
  STATUS status,
  USER_ID userId,
  INCOMMING_DATE incommingDate,
  APPROVED_DATE approvedDate,
  NOTES notes,
  RETURN_CODE returnCode
FROM CR_APPROVAL_DEPARTMENT
WHERE 1 = 1
