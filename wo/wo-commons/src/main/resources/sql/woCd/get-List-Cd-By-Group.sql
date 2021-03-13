SELECT  c.cd_id cdId,
        c.user_id userId,
        c.wo_group_id,
        u.username,
        u.fullname,
        u.email,
        u.mobile
FROM    wo_cd c,
        common_gnoc.users u
WHERE   c.user_id = u.user_id
