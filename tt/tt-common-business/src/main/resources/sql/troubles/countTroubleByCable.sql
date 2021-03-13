WITH tb AS
  (SELECT *
  FROM
    (SELECT ts.CREATED_TIME,
      ts.PRIORITY_ID,
      ts.INSERT_SOURCE,
      ts.LINE_CUT_CODE,
      ts.TROUBLE_CODE,
      ts.reason_id,
      ts.reason_name,
      ts.trouble_name,
      TO_CHAR(ts.begin_trouble_time,'dd/MM/yyyy HH24:MI:ss') beginTroubleTime,
      TO_CHAR(ts.end_trouble_time,'dd/MM/yyyy HH24:MI:ss') endTroubleTime,
      s.item_name ,
      ts.receive_unit_name,
      ts.ROOT_CAUSE rootCause,
      ts.WORK_ARROUND workArround
    FROM one_tm.troubles ts
    LEFT JOIN COMMON_GNOC.cat_item s
    ON ts.state           = s.item_id
    WHERE ts.CREATED_TIME > to_date(:startTime,'dd/MM/yyyy HH24:mi:ss')
    AND ts.CREATED_TIME   < to_date(:endTime,'dd/MM/yyyy HH24:mi:ss')
    ) temp
  WHERE 1=1
