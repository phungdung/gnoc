SELECT his.CHS_ID chsId,his.CR_ID crId,
  TO_CHAR(his.CHANGE_DATE,'dd/MM/yyyy HH24:mi:ss') changeDate
FROM CR_HIS his
WHERE his.STATUS    = 7
AND his.ACTION_CODE = 24
AND his.RETURN_CODE = 39
