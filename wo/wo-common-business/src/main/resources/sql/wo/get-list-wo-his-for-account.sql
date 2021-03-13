select d.WO_ID woId,d.CC_RESULT reasonLevel3Id, w.wo_code woCode,
w.WO_CONTENT woContent, w.WO_DESCRIPTION woDescription,
to_char(w.CREATE_DATE,'dd/MM/yyyy hh24:mi:ss') createTime,
to_char(w.FINISH_TIME,'dd/MM/yyyy hh24:mi:ss') finishTime,
w.COMMENT_COMPLETE commentComplete, d.customer_Phone customerPhone
from wfm.wo_detail d , wfm.wo w
where 1=1
and d.CREATE_DATE > sysdate -90
and d.ACCOUNT_ISDN in (:lstAcc)
and d.CC_RESULT is not null
and w.wo_id = d.wo_id
and w.status = 8
