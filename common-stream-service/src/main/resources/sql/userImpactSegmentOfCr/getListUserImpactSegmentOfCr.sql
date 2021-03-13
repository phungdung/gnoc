SELECT a.numcr numCr,
  a.change_responsible userId,
  userrole.username userName,
  userrole.role_name role,
  userimpact.impact_segment_name array ,
  b.unit_name unitName
FROM
  (SELECT COUNT ( * ) AS numcr,
    change_responsible,
    change_responsible_unit
  FROM open_pm.cr cr
  WHERE change_responsible IS NOT NULL
  AND state                IN (5, 6)

  compareDate

  GROUP BY change_responsible,
    change_responsible_unit
  ) a,
  (SELECT user_id, username , listagg(TO_CHAR(role_name), ',') within GROUP (ORDER BY user_id) AS role_name
  FROM
    (
      SELECT rs.role_name,
            ur.username,
            ur.user_id,
            rs.role_id
      FROM
      (SELECT us.username,
        us.user_id,
        rr.role_id
      FROM users us
      LEFT JOIN role_user rr
      ON us.user_id = rr.user_id
      ) ur
    INNER JOIN roles rs
    ON ur.role_id = rs.role_id

    listRoleReplace
    )

    GROUP BY user_id,
      username
    ) userrole,
    (SELECT user_id,
      username ,
      listagg(TO_CHAR(impact_segment_name), ',') within GROUP (
    ORDER BY user_id) AS impact_segment_name
    FROM
      (SELECT uut.username,
        uut.user_id,
        it.impact_segment_name,
        it.impact_segment_id
      FROM
        (SELECT us.username,
          us.user_id,
          ut.impact_segment_id
        FROM users us
        LEFT JOIN user_impact_segment ut
        ON us.user_id = ut.user_id
        ) uut
      INNER JOIN open_pm.impact_segment it
      ON uut.impact_segment_id = it.impact_segment_id

      impactSegmentReplace
      )
    GROUP BY user_id,
      username
    ) userimpact,
    unit b
  WHERE a.change_responsible    = userrole.user_id
  AND a.change_responsible      = userimpact.user_id
  AND a.change_responsible_unit = b.unit_id
  ORDER BY a.numcr ASC
