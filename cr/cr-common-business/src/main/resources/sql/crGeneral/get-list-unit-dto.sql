SELECT ut.unit_id unitId,
       ut.parent_unit_id,
       CASE
         WHEN ut.unit_code IS NULL
                 THEN ''
         ELSE TO_CHAR(ut.unit_code
                        || ' ('
                        || ut.unit_name
                        || ')')
           END AS unitName,
       CASE
         WHEN parentUt.unit_code IS NULL
                 THEN ''
         ELSE TO_CHAR(parentUt.unit_code
                        || ' ('
                        || parentUt.unit_name
                        || ')')
           END AS parentUnitName
FROM common_gnoc.unit ut
       LEFT JOIN common_gnoc.unit parentUt
         ON ut.parent_unit_id = parentUt.unit_id
WHERE ut.status      = 1
