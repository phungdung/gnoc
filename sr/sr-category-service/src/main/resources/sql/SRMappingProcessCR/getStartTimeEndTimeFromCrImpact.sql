WITH sr_data AS
  ( SELECT DISTINCT sr.sr_id,
    cata.service_code,
    cata.service_id
  FROM sr sr
  INNER JOIN sr_catalog cata
  ON sr.service_id = cata.service_id
  WHERE sr_id      = :p_sr_id
  ),
  mapping_data AS
  (SELECT mapp.service_code,
    data.service_id ,
    data.sr_id ,
    mapp.cr_process_parent_id,
    mapp.cr_process_id,
    a.ife_code,
    a.ife_name,
    CASE
      WHEN LENGTH(a.start_time) < 2
      THEN '0'
        || a.start_time
      ELSE TO_CHAR(a.start_time)
    END start_time,
    CASE
      WHEN LENGTH(a.end_time) < 2
      THEN '0'
        || a.end_time
      ELSE TO_CHAR(a.end_time)
    END end_time
  FROM sr_mapping_process_cr mapp
  INNER JOIN sr_data data
  ON mapp.service_code = data.service_code
  INNER JOIN
    (SELECT DISTINCT impact.ife_id ,
      impact.ife_code,
      impact.ife_name,
      impact.start_time ,
      impact.end_time ,
      cr.cr_process_id
    FROM cr_impact_frame impact
    INNER JOIN cr_process cr
    ON impact.ife_id          = cr.impact_type
    WHERE cr.cr_process_id    = :p_cr_process_id
    AND cr.is_active          = 1
    ) a ON ( (mapp.cr_process_id is not null and mapp.cr_process_id = a.cr_process_id) OR  (mapp.cr_process_id is null and mapp.cr_process_parent_id = a.cr_process_id ))
  ) ,
  temp_data AS
  (SELECT mapp.sr_id ,
    mapp.cr_process_id ,
    mapp.ife_code,
    mapp.ife_name,
    mapp.start_time ,
    mapp.end_time ,
    :p_execution_time execution_time,
    :p_execution_end_time execution_end_time
  FROM mapping_data mapp
  ) ,
  raw_data AS
  (SELECT temp.sr_id ,
    temp.cr_process_id,
    temp.ife_code,
    temp.ife_name,
    temp.start_time ,
    temp.end_time ,
    CASE
     WHEN  (temp.start_time > temp.end_time AND to_char(temp.execution_time,'HH24') < to_char(temp.execution_end_time,'HH24') )
      THEN (TO_CHAR(temp.execution_time - 1,'dd-mm-yyyy'))
        || ' '
        || temp.start_time
        || ':00'
      ELSE   TO_CHAR(temp.execution_time,'dd-mm-yyyy')
      || ' '
      || temp.start_time
      || ':00'
      END execution_time,

    CASE
      WHEN (temp.start_time < temp.end_time OR temp.execution_time < temp.execution_end_time)
      THEN TO_CHAR(temp.execution_end_time,'dd-mm-yyyy')
        || ' '
        ||temp.end_time
        || ':59'
      ELSE (TO_CHAR(temp.execution_end_time + 1,'dd-mm-yyyy'))
        || ' '
        || temp.end_time
        || ':59'
    END execution_end_time
  FROM temp_data temp
  )
SELECT a.sr_id srid,
  a.cr_process_id crprocessid,
  a.ife_code ifecode,
  a.ife_name ifename,
  TO_CHAR(a.start_time
  ||':00') starttime ,
  TO_CHAR(a.end_time
  ||':59') endtime ,
  CASE
    WHEN :p_execution_time    >= to_date(a.execution_time ,'dd-mm-yyyy hh24:mi:ss')
    AND :p_execution_end_time <= to_date(a.execution_end_time ,'dd-mm-yyyy hh24:mi:ss')
    THEN 'OK'
    ELSE 'NOK'
  END checkdata
FROM raw_data a
