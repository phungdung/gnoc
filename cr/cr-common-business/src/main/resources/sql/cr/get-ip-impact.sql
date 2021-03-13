select a.device_id, b.ip_id, b.ip
     from common_gnoc.infra_device a,common_gnoc.infra_ip b
     where a.device_id=b.device_id and b.ip in (
