SELECT TO_CHAR(b.EARLIEST_START_TIME, 'dd/MM/yyyy HH:mm:ss') as earliestStartTime, TO_CHAR(b.LATEST_END_TIME, 'dd/MM/yyyy HH:mm:ss') as latestEndTime
from OPEN_PM.CR a JOIN OPEN_PM.CR_HIS b ON a.CR_ID = b.CR_ID
where 1=1
  AND a.CR_ID = :crId
  AND b.STATUS = :status
  AND TO_CHAR(ADD_MONTHS(sysdate,-2) ,'yyyyMM') <=  TO_CHAR(b.LATEST_END_TIME,'yyyyMM')
