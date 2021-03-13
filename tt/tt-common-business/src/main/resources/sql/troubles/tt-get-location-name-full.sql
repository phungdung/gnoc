SELECT   SYS_CONNECT_BY_PATH (a.location_name, ' / ') locationNameFull
      FROM       common_gnoc.cat_location a
            LEFT JOIN
                common_gnoc.cat_location p
             ON p.location_id = a.parent_id
     WHERE   a.location_id = :locationId
 START WITH   a.parent_id IS NULL
 CONNECT BY   PRIOR a.location_id = a.parent_id
