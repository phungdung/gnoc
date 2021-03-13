select username from COMMON_GNOC.USERS b1
join COMMON_GNOC.ROLE_USER b2
on b1.user_id = b2.USER_ID
join COMMON_GNOC.ROLES b3
on b2.ROLE_ID = b3.ROLE_ID
where b1.USERNAME = :username and b3.ROLE_CODE = :roleCode
