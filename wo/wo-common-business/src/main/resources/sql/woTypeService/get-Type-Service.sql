select  s.is_check_qos_internet isCheckQosInternet,
        s.is_check_qos_th isCheckQosTh,
        s.is_check_qr_code isCheckQrCode
from    wo_type_service s
where   s.wo_type_id = :woTypeId
and     s.service_id = :serviceId
