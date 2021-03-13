WITH tmp AS
  (SELECT *
  FROM
    (SELECT *
    FROM open_pm.sr_his his
    WHERE sr_id = :srId
    ORDER BY created_time DESC
    )
  WHERE rownum = 1
  )
SELECT COUNT(*) totalRow
FROM tmp,
  open_pm.sr_worklog w
WHERE tmp.sr_id     = w.sr_id
AND w.created_time >= tmp.created_time
