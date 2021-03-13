SELECT cab.impact_segment_id impactSegmentId,
       cab.execute_unit_id executeUnitId,
       cab.creation_unit_id createUnitId,
       us.user_id userCab,
       us.username,
       0 numberCr
FROM cr_cab_users cab
       LEFT JOIN common_gnoc.users us
         ON cab.user_id    =us.user_id
WHERE us.user_id IS NOT NULL
