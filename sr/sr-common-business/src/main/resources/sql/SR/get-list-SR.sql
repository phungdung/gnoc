WITH tmp_his AS
  (SELECT sr_id,
    next_time,
    new_time
  FROM
    (SELECT n.sr_id,
      n.sr_status,
      MAX(n.created_time)   AS New_Time,
      MIN (tn.created_time) AS Next_Time
    FROM open_pm.sr_his n,
      open_pm.sr_his tn
    WHERE n.sr_id     = tn.sr_id
    AND n.sr_status   = 'New'
    AND tn.sr_status IN
      (SELECT config_code
      FROM open_pm.SR_CONFIG
      WHERE CONFIG_GROUP ='STATUS_RECEIVE_SLA'
      ) limitTimeHisFrom
    GROUP BY n.sr_id,
      n.sr_status
    )
  ) ,
  tmp_dayOff AS (
  (SELECT t1.sr_id,
    COUNT(t2.date_off) dateOff,
    t2.location_id
  FROM OPEN_PM.SR t1
  LEFT JOIN
    (SELECT a.date_off date_off,
      b.location_id
    FROM common_gnoc.day_off a
    LEFT JOIN common_gnoc.cat_location b
    ON a.nation             = b.location_code
    ) t2 ON t1.country      = t2.location_id
  WHERE (t2.date_off) <= TRUNC(
    CASE
      WHEN t1.end_time < SYSDATE
      THEN SYSDATE
      ELSE t1.end_time
    END )
  AND (t2.date_off) >= TRUNC(
    CASE
      WHEN t1.start_time < SYSDATE
      THEN SYSDATE
      ELSE t1.start_time
    END) limitTimeDayOffFrom
  GROUP BY t1.sr_id,
    t2.location_id
  ) ) ,
  tmp_actual AS (
  (SELECT sr.sr_id,
    ROUND(CAST(MAX(his.created_time) AS DATE) - CAST(MIN(his.created_time) AS DATE),2) actualExecutionTime
  FROM open_pm.sr sr ,
    open_pm.sr_his his
  WHERE sr.sr_id         = his.sr_id
  AND sr.status         IN ('Concluded','Closed')
  AND his.sr_status NOT IN ('Draft','New','Closed') limitTimeFrom
  GROUP BY sr.sr_id
  ) ) ,
  tmp_concluded AS
  (SELECT sr.sr_id,
    MIN(his.created_time) concluded_time
  FROM open_pm.sr sr ,
    open_pm.sr_his his
  WHERE sr.sr_id    = his.sr_id
  AND his.sr_status = 'Concluded' limitTimeFrom
  GROUP BY sr.sr_id
  ) ,
  tmp_reject_cancel AS
  (SELECT sr_id,
    new_time,
    MIN(created_time) rOc_time
  FROM
    (SELECT sr_id,
      new_time,
      created_time
    FROM
      (SELECT n.sr_id,
        n.sr_status ,
        MAX(n.created_time) AS New_Time ,
        tn.created_time
      FROM open_pm.sr_his n,
        open_pm.sr_his tn
      WHERE n.sr_id     = tn.sr_id
      AND n.sr_status   = 'New'
      AND tn.sr_status IN ('Rejected','Cancelled') limitTimeHisFrom
      GROUP BY n.sr_id,
        n.sr_status,
        tn.created_time
      )
    WHERE created_time > new_time
    )
  GROUP BY sr_id,
    new_time
  ) ,
  tmp_language AS
  (SELECT APPLIED_BUSSINESS,
    BUSSINESS_ID,
    LEE_VALUE,
    LEE_LOCALE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE
  WHERE APPLIED_SYSTEM = 2
  AND APPLIED_BUSSINESS BETWEEN 20 AND 21
  ) ,
  main_data AS
  (SELECT T1.SR_ID srId ,
    T1.SR_CODE srCode ,
    T1.COUNTRY country ,
    T1.SERVICE_ARRAY serviceArray ,
    CASE
      WHEN leArray.LEE_LOCALE = :locale
      THEN (
        CASE
          WHEN leArray.LEE_VALUE IS NOT NULL
          THEN leArray.LEE_VALUE
          ELSE CAST(T6.CONFIG_NAME AS NVARCHAR2(2000))
        END )
      ELSE CAST(T6.CONFIG_NAME AS NVARCHAR2(2000))
    END serviceArrayName ,
    T1.SERVICE_GROUP serviceGroup ,
    CASE
      WHEN leGroup.LEE_LOCALE = :locale
      THEN (
        CASE
          WHEN leGroup.LEE_VALUE IS NOT NULL
          THEN leGroup.LEE_VALUE
          ELSE CAST(T7.CONFIG_NAME AS NVARCHAR2(2000))
        END )
      ELSE CAST(T7.CONFIG_NAME AS NVARCHAR2(2000))
    END serviceGroupName ,
    CASE
      WHEN leService.LEE_LOCALE = :locale
      THEN (
        CASE
          WHEN leService.LEE_VALUE IS NOT NULL
          THEN leService.LEE_VALUE
          ELSE CAST(T2.SERVICE_NAME AS NVARCHAR2(2000))
        END )
      ELSE CAST(T2.SERVICE_NAME AS NVARCHAR2(2000))
    END serviceName ,
    T1.TITLE title ,
    T1.STATUS status ,
    T3.CONFIG_NAME statusName ,
    T1.CREATED_USER createdUser,
    tU2.PATHNAME checkingUnit ,
    T2.SERVICE_ID serviceId ,
    T2.SERVICE_CODE serviceCode ,
    T2.REPLY_TIME replyTime ,
    T2.CR_WO_CREATE_TIME crWoCreatTime ,
    T8.STATUS_RENEW statusRenew ,
    TO_CHAR(T8.END_TIME_RENEW,'dd/MM/yyyy HH24:mi:ss') dayRenew ,
    TO_CHAR(T1.SEND_DATE  + :double_offset * interval '1' hour,'dd/MM/yyyy HH24:mi') sendDate ,
    TO_CHAR(T1.START_TIME + :double_offset * interval '1' hour,'dd/MM/yyyy HH24:mi') startTime ,
    TO_CHAR(T1.END_TIME   + :double_offset * interval '1' hour,'dd/MM/yyyy HH24:mi') endTime ,
    T1.SR_USER srUser ,
    T1.SR_UNIT srUnit ,
    TO_CHAR(T1.CREATED_TIME + :double_offset * interval '1' hour,'dd/MM/yyyy HH24:mi') createdTime ,
    TO_CHAR(T1.UPDATED_TIME + :double_offset * interval '1' hour,'dd/MM/yyyy HH24:mi') updatedTime ,
    T2.EXECUTION_TIME executionTime ,
    T3.PARENT_CODE STATUSORDER ,
    ROUND (
    CASE
      WHEN concluded.concluded_time IS NOT NULL
      THEN CAST(T1.END_TIME AS DATE) - CAST(concluded.concluded_time AS DATE)
      WHEN rOc.rOc_time IS NOT NULL
      THEN CAST(T1.END_TIME AS DATE) - CAST(rOc.rOc_time AS DATE)
      ELSE
        CASE
          WHEN T1.START_TIME < sysdate
          THEN CAST(T1.END_TIME AS DATE) - CAST(sysdate AS DATE)
          ELSE CAST(T1.END_TIME AS DATE) - CAST(T1.START_TIME AS DATE)
        END
    END,2) -
    CASE
      WHEN dayOff.dateOff IS NULL
      THEN 0
      ELSE dayOff.dateOff
    END remainExecutionTime ,
    TO_CHAR(his.next_time,'dd/MM/yyyy HH24:mi:ss') evaluateReplyTime ,
    tU.PATHNAME pathSrUnit ,
    actual.actualExecutionTime -
    CASE
      WHEN dayOff.dateOff IS NULL
      THEN 0
      ELSE dayOff.dateOff
    END actualExecutionTime ,
    T1.PARENT_CODE parentCode ,
    T1.DESCRIPTION description ,
    T1.ROLE_CODE roleCode ,
    T1.INSERT_SOURCE insertSource ,
    T1.OTHER_SYSTEM_CODE ,
    T1.COUNT_NOK countNok,
    T2.FLOW_EXECUTE flowExecute ,
    tFlow.FLOW_NAME flowExecuteName,
    T1.CR_NUMBER crNumber,
    CASE
    WHEN T1.REVIEW_ID = 1
    THEN '*'
    WHEN T1.REVIEW_ID = 2
    THEN '**'
    WHEN T1.REVIEW_ID = 3
    THEN '***'
    WHEN T1.REVIEW_ID = 4
    THEN '****'
    WHEN T1.REVIEW_ID = 5
    THEN '*****'
    END reviewViews,
    c.LOCATION_NAME countryName,
    T4.MOBILE MOBILE
  FROM OPEN_PM.SR T1
  LEFT JOIN open_pm.sr_renew T8
  ON T1.SR_ID = T8.SR_ID
  INNER JOIN OPEN_PM.SR_CATALOG T2
  ON T1.SERVICE_ID = T2.SERVICE_ID
  LEFT JOIN tmp_language leService
  ON T2.SERVICE_ID                = leService.BUSSINESS_ID
  AND leService.APPLIED_BUSSINESS = 21
  LEFT JOIN OPEN_PM.SR_FLOW_EXECUTE tFlow
  ON T2.FLOW_EXECUTE = tFlow.FLOW_ID
  INNER JOIN OPEN_PM.SR_CONFIG T3
  ON T1.STATUS        = T3.CONFIG_CODE
  AND T3.CONFIG_GROUP = 'STATUS'
  LEFT JOIN COMMON_GNOC.USERS T4
  ON T1.CREATED_USER = T4.USERNAME
  LEFT JOIN COMMON_GNOC.UNIT T5
  ON T4.UNIT_ID = T5.UNIT_ID
  INNER JOIN OPEN_PM.SR_CONFIG T6
  ON T6.CONFIG_CODE   = T1.SERVICE_ARRAY
  AND T6.CONFIG_GROUP = 'SERVICE_ARRAY'
  LEFT JOIN tmp_language leArray
  ON T6.CONFIG_ID               = leArray.BUSSINESS_ID
  AND leArray.APPLIED_BUSSINESS = 20
  INNER JOIN OPEN_PM.SR_CONFIG T7
  ON T7.CONFIG_CODE   = T1.SERVICE_GROUP
  --AND T7.CONFIG_GROUP = 'SERVICE_GROUP'
  --AND T6.CONFIG_CODE  = T7.PARENT_CODE
  AND T1.SERVICE_ARRAY  = T7.PARENT_CODE
  LEFT JOIN tmp_language leGroup
  ON T7.CONFIG_ID               = leGroup.BUSSINESS_ID
  AND leGroup.APPLIED_BUSSINESS = 20
  LEFT JOIN tmp_his his
  ON his.sr_id = t1.sr_id
  LEFT JOIN COMMON_GNOC.V_UNIT_AS_TREE tU
  ON tU.UNIT_ID = t1.SR_UNIT
  LEFT JOIN COMMON_GNOC.V_UNIT_AS_TREE tU2
  ON tU2.UNIT_ID = T5.UNIT_ID
  LEFT JOIN tmp_dayOff dayOff
  ON T1.sr_id    = dayOff.sr_id
  AND T1.country = dayOff.location_id
  LEFT JOIN tmp_actual actual
  ON actual.sr_id = T1.sr_id
  LEFT JOIN tmp_concluded concluded
  ON T1.SR_ID = concluded.SR_ID
  LEFT JOIN tmp_reject_cancel rOc
  ON T1.SR_ID = rOc.SR_ID
  LEFT JOIN common_gnoc.cat_location c
  ON c.LOCATION_ID=T1.COUNTRY
  $closedRequestTime$
  )
SELECT SRID,
  SRCODE,
  COUNTRY,
  SERVICEARRAY,
  SERVICEARRAYNAME,
  SERVICEGROUP,
  SERVICEGROUPNAME,
  SERVICENAME,
  TITLE,
  STATUS,
  STATUSNAME,
  CREATEDUSER,
  case
    when CREATEDUSER is null
    then ''
    else  CREATEDUSER || ' ('||MOBILE||')'
  end createdUserMobile ,
  CHECKINGUNIT,
  SERVICEID,
  SERVICECODE,
  REPLYTIME,
  CRWOCREATTIME,
  STATUSRENEW,
  DAYRENEW,
  to_date(SENDDATE,'dd/MM/yyyy HH24:mi:ss') SENDDATE,
  to_date(STARTTIME,'dd/MM/yyyy HH24:mi:ss') STARTTIME,
  to_date(ENDTIME,'dd/MM/yyyy HH24:mi:ss') ENDTIME,
  SRUSER,
  SRUNIT,
  to_date(CREATEDTIME,'dd/MM/yyyy HH24:mi:ss') CREATEDTIME,
  to_date(UPDATEDTIME,'dd/MM/yyyy HH24:mi:ss') UPDATEDTIME,
  EXECUTIONTIME,
  PARENTCODE,
  STATUSORDER,
  REMAINEXECUTIONTIME,
  EVALUATEREPLYTIME,
  PATHSRUNIT,
  ACTUALEXECUTIONTIME,
  DESCRIPTION,
  ROLECODE,
  INSERTSOURCE,
  FLOWEXECUTE,
  FLOWEXECUTENAME,
  COUNTRYNAME,
  crNumber,
  reviewViews,
  countNok
FROM main_data T1
WHERE 1=1
