SELECT ass.SERVICE_CODE affectedService ,
  cain.DEVICE_CODE description ,
  cr.title title,
  cr.cr_id crId,
  cr.cr_number crNumber,
  ccay.CHILDREN_NAME childImpactSegment,
  cr.PRIORITY priority,
  cr.risk,
  ROUND((to_date(TO_CHAR(NVL(cr.DISTURBANCE_END_TIME, cr.LATEST_START_TIME), 'dd/MM/yyyy HH24:MI:ss'), 'dd/MM/yyyy HH24:MI:ss') - to_date(TO_CHAR(NVL(cr.DISTURBANCE_START_TIME, cr.EARLIEST_START_TIME), 'dd/MM/yyyy HH24:MI:ss'), 'dd/MM/yyyy HH24:MI:ss')) * 24 * 60,0) responeTime,
  cr.EARLIEST_START_TIME earliestStartTime,
  cr.LATEST_START_TIME latestStartTime,
  ist.IMPACT_SEGMENT_NAME impactSegment
FROM open_pm.cr cr
 LEFT JOIN COMMON_GNOC.CFG_CHILD_ARRAY ccay on cr.CHILD_IMPACT_SEGMENT = ccay.CHILDREN_ID
 LEFT JOIN OPEN_PM.IMPACT_SEGMENT ist on cr.IMPACT_SEGMENT = ist.IMPACT_SEGMENT_ID
LEFT JOIN
  (SELECT cr_id,
    rtrim(xmlagg(XMLELEMENT(e,SERVICE_CODE,',').EXTRACT('//text()') ).GetClobVal(),',') AS SERVICE_CODE
  FROM
    (SELECT a.CR_ID,
      b.SERVICE_CODE,
      a.INSERT_TIME
    FROM OPEN_PM.CR_AFFECTED_SERVICE_DETAILS a
    INNER JOIN OPEN_PM.AFFECTED_SERVICES b
    ON a.AFFECTED_SERVICE_ID = b.AFFECTED_SERVICE_ID
     WHERE CR_ID             IN (:lstCr1)
    )
  GROUP BY cr_id
  ) ass ON cr.cr_id = ass.cr_id
LEFT JOIN
  (SELECT cr_id,
    RTRIM(XMLAGG(XMLELEMENT(e,DEVICE_CODE,',').EXTRACT('//text()')
  ORDER BY DEVICE_CODE).GetClobVal(),',') DEVICE_CODE
  FROM
    ( SELECT DISTINCT(DEVICE_CODE),
      cr_id
    FROM
      (SELECT CR_ID,
        DEVICE_CODE,
        INSERT_TIME
      FROM OPEN_PM.CR_AFFECTED_NODES
       WHERE CR_ID IN (:lstCr2)
      UNION ALL
      SELECT CR_ID,
        DEVICE_CODE,
        INSERT_TIME
      FROM OPEN_PM.CR_IMPACTED_NODES
      WHERE CR_ID IN (:lstCr3)
      )
    GROUP BY cr_id,
      DEVICE_CODE
    )
  GROUP BY cr_id
  ) cain
ON cr.cr_id                    = cain.cr_id
WHERE cr.CR_ID IN (:lstCr4)
