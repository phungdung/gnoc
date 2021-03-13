select  w.wo_code woCode,
        to_char(w.CREATE_DATE,'dd/MM/yyyy HH24:mi:ss') createDate,
        w.WO_CONTENT woContent,
        w.wo_id woId,
        d.ACCOUNT_ISDN accountIsdn,
        w.wo_Description woDescription,
        to_char(w.start_time,'dd/MM/yyyy HH24:mi:ss') startTime,
        to_char(w.end_time,'dd/MM/yyyy HH24:mi:ss') endTime
from    wfm.wo w,
        wfm.wo_detail d
where   d.wo_id = w.wo_id
and     w.wo_system = 'SPM'
and     w.create_date > sysdate - :numDate
and     w.status in (0,1,3,4,5,9)
and     d.ACCOUNT_ISDN in (:lstAccount)
