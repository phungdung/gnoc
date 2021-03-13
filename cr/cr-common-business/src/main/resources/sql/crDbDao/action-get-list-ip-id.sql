SELECT ip_id scopeId
FROM cr_impacted_nodes cins
WHERE insert_time>TRUNC(to_date(:insert_time, 'dd/MM/yyyy HH24:mi:ss'))
  AND cr_id        = :crId
