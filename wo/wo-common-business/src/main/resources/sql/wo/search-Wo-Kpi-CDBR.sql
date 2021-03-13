select  u.username username,
        d.account_isdn accountIsdn,
        s.service_code serviceId,
        d.cc_service_id ccServiceId,
        d.tel_service_id telServiceId,
        decode(d.infra_type,1,:copperCable,2,:coaxialCable,3,'AON',4,'GPON','N/A') productCode,
        to_char(a.end_time,'dd/MM/yyyy HH24:mi:ss') endTime,
        to_char(a.finish_time,'dd/MM/yyyy HH24:mi:ss') finishTime
from    wo a,
        common_gnoc.users u,
        wo_detail d,
        common_gnoc.cat_service s
where   a.ft_id =u.user_id and a.wo_id = d.wo_id(+)
and     d.service_id = s.service_id(+)
and     to_char(a.wo_type_id) in (select a.value from common_gnoc.config_property a where a.key = :key)
and     a.finish_time >= TO_TIMESTAMP(:startTime,'dd/mm/yyyy hh24:mi:ss')
and     a.finish_time <= TO_TIMESTAMP(:endTime,'dd/mm/yyyy hh24:mi:ss')
and     a.wo_system = 'SPM'
