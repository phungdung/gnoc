SELECT b.username fieldName,
  COUNT(*) fieldValue,
  'A' AS fieldKey
FROM wo a,
  common_gnoc.users b
WHERE a.finish_time <= a.end_time
AND a.result        IS NOT NULL
AND a.ft_id         IS NOT NULL
AND a.ft_id          = b.user_id
AND a.end_time      >= to_date(:p_start_time,'dd/MM/yyyy')
AND a.end_time      <= to_date(:p_end_time,'dd/MM/yyyy')
GROUP BY b.username
UNION ALL
SELECT b.username fieldName,
  COUNT(*) fieldValue,
  'B' AS fieldKey
FROM wo a,
  common_gnoc.users b
WHERE a.finish_time > a.end_time
AND a.result       IS NOT NULL
AND a.ft_id        IS NOT NULL
AND a.ft_id         = b.user_id
AND a.end_time     >= to_date(:p_start_time,'dd/MM/yyyy')
AND a.finish_time  <= to_date(:p_end_time,'dd/MM/yyyy')
GROUP BY b.username
UNION ALL
SELECT b.username fieldName,
  COUNT(*) fieldValue,
  'C' AS fieldKey
FROM wo a,
  common_gnoc.users b
WHERE (a.finish_time IS NULL
OR a.finish_time      > to_date(:p_end_time,'dd/MM/yyyy'))
AND a.ft_id          IS NOT NULL
AND a.ft_id           = b.user_id
AND a.end_time       >= to_date(:p_start_time,'dd/MM/yyyy')
AND a.end_time       <= to_date(:p_end_time,'dd/MM/yyyy')
GROUP BY b.username
UNION ALL
SELECT b.username fieldName,
  COUNT(*) fieldValue,
  'D' AS fieldKey
FROM wo a,
  common_gnoc.users b
WHERE ((a.finish_time >= a.end_time + 5
AND a.result          IS NOT NULL
AND a.finish_time     <= to_date(:p_end_time,'dd/MM/yyyy')
AND a.finish_time     >= to_date(:p_start_time,'dd/MM/yyyy'))
OR ((a.finish_time    IS NULL
OR a.finish_time       > to_date(:p_end_time,'dd/MM/yyyy'))
AND a.end_time        <= to_date(:p_end_time,'dd/MM/yyyy') - 5))
AND a.ft_id           IS NOT NULL
AND a.ft_id            = b.user_id
GROUP BY b.username
UNION ALL
SELECT b.username fieldName,
  COUNT(*) fieldValue,
  'E' AS fieldKey
FROM wo a,
  common_gnoc.users b
WHERE a.finish_time < a.end_time + 5
AND a.result       IS NOT NULL
AND a.ft_id        IS NOT NULL
AND a.ft_id         = b.user_id
AND a.end_time     >= to_date(:p_start_time,'dd/MM/yyyy')
AND a.finish_time  <= to_date(:p_end_time,'dd/MM/yyyy')
AND a.end_time     <= to_date(:p_end_time,'dd/MM/yyyy')
GROUP BY b.username
UNION ALL
SELECT b.username fieldName,
  COUNT(*) fieldValue,
  'F' AS fieldKey
FROM wo a,
  common_gnoc.users b
WHERE (a.finish_time IS NULL
OR a.finish_time      > to_date(:p_end_time,'dd/MM/yyyy'))
AND a.end_time        > to_date(:p_end_time,'dd/MM/yyyy') - 5
AND a.ft_id          IS NOT NULL
AND a.ft_id           = b.user_id
AND a.end_time       <= to_date(:p_end_time,'dd/MM/yyyy')
GROUP BY b.username
