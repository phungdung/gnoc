WITH tmp_closed AS
  (SELECT sr.sr_id,
    MAX(his.created_time) closed_time
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
  ) ),
  tmp_data AS
  (SELECT a.SR_ID srId,
    a.closed_time closedTime,
    b.new_time newTime,
    CASE
      WHEN c.dateOff IS NULL
      THEN ROUND(CAST((a.closed_time) AS DATE)  - CAST((b.new_time) AS DATE),2)
      ELSE (ROUND(CAST((a.closed_time) AS DATE) - CAST((b.new_time) AS DATE),2)-c.dateOff)
    END totalSRProcessTime,
    d.CR_WO_CREATE_TIME
  FROM tmp_closed_time a
  JOIN new_time b
  ON a.sr_id = b.sr_id
  LEFT JOIN tmp_dayOff c
  ON a.sr_id = c.sr_id
  LEFT JOIN
    (SELECT sr.SR_ID,
      NVL(sc.CR_WO_CREATE_TIME,0) CR_WO_CREATE_TIME
    FROM SR sr
    LEFT JOIN SR_CATALOG sc
    ON sr.SERVICE_ID= sc.SERVICE_ID
    ) d ON d.SR_ID  = a.SR_ID
  )
SELECT data.srId srId,
  CASE
    WHEN data.totalSRProcessTime <= data.CR_WO_CREATE_TIME
    THEN :onTime
    ELSE :slow
  END createCrSlow
FROM tmp_data data
Left join open_pm.sr t1
on data.srId = t1.sr_id
WHERE 1 =1
AND t1.cr_number is not null
