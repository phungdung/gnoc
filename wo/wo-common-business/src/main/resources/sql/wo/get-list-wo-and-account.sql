SELECT a.wo_code woCode,
  u.username ftId,
  u.mobile customerName,
  a.wo_content woContent,
  d.account_isdn accountIsdn
FROM wfm.wo a,
  common_gnoc.users u,
  wfm.wo_detail d
WHERE a.ft_id   = u.user_id
AND a.wo_id     = d.wo_id
AND a.status    = 8
AND a.WO_SYSTEM = 'SPM' 
