WITH tmp_closed AS
  (SELECT sr.sr_id,
    MIN(his.created_time) closed_time
  FROM open_pm.sr sr ,
    open_pm.sr_his his
  WHERE sr.sr_id    = his.sr_id
  AND his.sr_status =:status
  GROUP BY sr.sr_id
  ),
  tmp_sr AS
  (SELECT sr_id,
    status,
    start_time,
    end_time,
    country
  FROM open_pm.SR
  WHERE status =:status
  ) ,
  tmp_closed_time AS
  ( SELECT b.* FROM tmp_sr a JOIN tmp_closed b ON a.sr_id = b.sr_id
  ),
  new_time AS
  (SELECT MIN(created_time) new_time,
    sr_id
  FROM open_pm.sr_his
  WHERE sr_id IN
    (SELECT sr_id FROM tmp_closed_time
    )
  AND sr_status = 'New'
  GROUP BY sr_id
  ),
  tmp_dayOff AS (
  (SELECT t1.sr_id,
    COUNT(t2.date_off) dateOff,
    t2.location_id
  FROM tmp_sr t1
  INNER JOIN tmp_closed_time c
  ON t1.sr_id = c.sr_id
  INNER JOIN new_time d
  ON t1.sr_id = d.sr_id
  LEFT JOIN
    (SELECT a.date_off date_off,
      b.location_id
    FROM common_gnoc.day_off a
    LEFT JOIN common_gnoc.cat_location b
    ON a.nation             = b.location_code
    ) t2 ON t1.country      = t2.location_id
  WHERE TRUNC(t2.date_off) <= TRUNC(c.closed_time)
  AND TRUNC(t2.date_off)   >= TRUNC(d.new_time)
  GROUP BY t1.sr_id,
    t2.location_id
  ) )
SELECT a.SR_ID srId,
  a.closed_time closedTime,
  b.new_time newTime,
  CASE
    WHEN c.dateOff IS NULL
    THEN ROUND(CAST((a.closed_time) AS DATE)  - CAST((b.new_time) AS DATE),2)
    ELSE (ROUND(CAST((a.closed_time) AS DATE) - CAST((b.new_time) AS DATE),2)-c.dateOff)
  END totalSRProcessTime
FROM tmp_closed_time a
JOIN new_time b
ON a.sr_id = b.sr_id
LEFT JOIN tmp_dayOff c
ON a.sr_id = c.sr_id
WHERE 1    =1
