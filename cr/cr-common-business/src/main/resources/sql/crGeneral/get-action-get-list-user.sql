SELECT us.username AS username,
       us.user_id       AS userId,
       ut.unit_id       AS unitId,
       ut.unit_Code     AS unitCode,
       us.staff_Code    AS staffCode,
       us.mobile        AS mobile,
       us.email         AS email,
       rs.role_code     AS roleCode,
       rs.role_name     AS roleName,
       CASE
         WHEN us.username IS NULL
                 THEN ''
         ELSE TO_CHAR(us.username
                        || ' ('
                        ||us.fullname
                        ||')')
           END AS fullname,
       CASE
         WHEN ut.unit_code IS NULL
                 THEN ''
         ELSE TO_CHAR(ut.unit_code
                        || ' ('
                        ||ut.unit_name
                        ||')')
           END AS unitName
FROM common_gnoc.users us
       LEFT JOIN common_gnoc.unit ut
         ON ut.unit_id = us.unit_id
       LEFT JOIN common_gnoc.role_user ru
         ON ru.user_id = us.user_id
       LEFT JOIN common_gnoc.roles rs
         ON ru.role_id   = rs.role_id
WHERE ut.status = 1
  AND us.is_enable=1
