select  s.user_id userId,
        s.username,
        s.fullname,
        s.mobile,
        s.email,
        u.dept_full unitName
from    common_gnoc.users s,
        common_gnoc.v_unit u
where   s.unit_id = u.unit_id
and     u.unit_id in (select t.unit_id
                      from common_gnoc.unit t
                      where level < 50 start with t.unit_id in (select g.unit_id
                                                                from  wfm.wo_cd_group_unit g,
                                                                      wfm.wo_cd c
                                                                where g.cd_group_id = c.wo_group_id
                                                                and   c.user_id = :userId)
                            connect by prior t.unit_id = t.parent_unit_id)
and     (lower(s.username) like :keyword escape '\'
        or lower(s.fullname) like :keyword  escape '\'
        or lower(s.mobile) like :keyword escape '\'
        or lower(s.email) like :keyword escape '\'
        or lower(u.dept_full) like :keyword escape '\')
group by s.user_id, s.username, s.fullname, s.mobile, s.email, u.dept_full
