select  a.wo_code woCode,
        t.WO_TYPE_NAME woTypeName,
        t.WO_TYPE_ID woTypeId,
        a.STATUS status,
        DECODE(a.status,
                0, :unassigned,
                1, :assigned,
                2, :reject,
                3, :dispatch,
                4, :accept,
                5, :inProcess,
                7, :draft,
                6, :closedFt,
                8, :closedCd,
                9, :pending
          ) statusName,
        a.WO_SYSTEM woSystem,
        to_char(a.CREATE_DATE,'dd/MM/yyyy HH24:mi:ss') createDate,
        g.WO_GROUP_NAME woGroupName,
        u.username ftName,
        p.PRIORITY_ID priorityId,
        p.PRIORITY_NAME priorityName,
        to_char(a.START_TIME,'dd/MM/yyyy HH24:mi:ss') startTime,
        to_char(a.COMPLETED_TIME,'dd/MM/yyyy HH24:mi:ss') completedTime,
        to_char(a.FINISH_TIME,'dd/MM/yyyy HH24:mi:ss') finishTime,
        d.ACCOUNT_ISDN accountIsdn
from  wfm.wo a,
      wfm.wo_type t,
      wfm.wo_cd_group g,
      common_gnoc.users u,
      wfm.wo_priority p,
      wfm.wo_detail d
where a.WO_TYPE_ID = t.WO_TYPE_ID
and   a.CREATE_DATE >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss')
and   a.CREATE_DATE <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss')
and   lower(t.wo_type_code) = :woTypeCode
and   a.cd_id = g.wo_group_id
and   a.ft_id = u.user_id(+)
and   a.PRIORITY_ID = p.PRIORITY_ID
and   a.wo_id = d.WO_ID
