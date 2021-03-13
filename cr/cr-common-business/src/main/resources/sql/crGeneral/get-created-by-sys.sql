SELECT objectCode AS valueStr,
       system_id       AS secondValue,
       '('
              ||systemCode
              ||') '
              ||objectCode AS displayStr
FROM
     (SELECT ccfoss.object_id,
             ccfoss.system_id,
             ps.problem_code,
             ts.trouble_code,
             mr.mr_code,
             CASE ccfoss.system_id
                    WHEN 1
                       THEN mr.mr_code
                    WHEN 2
                       THEN TO_NCHAR(ps.problem_code)
                    WHEN 3
                       THEN TO_NCHAR(ts.trouble_code)
                    WHEN 4
                       THEN TO_NCHAR(wo.wo_code)
                    WHEN 5
                       THEN TO_NCHAR(sr.SR_CODE)
                    WHEN 6
                       THEN TO_NCHAR(ccfoss.OBJECT_CODE)
                    WHEN 7
                       THEN TO_NCHAR(rk.RISK_CODE)
                    ELSE TO_NCHAR('')
                 END AS objectCode,
             CASE ccfoss.system_id
                    WHEN 1
                       THEN 'MR'
                    WHEN 2
                       THEN 'PT'
                    WHEN 3
                       THEN 'TT'
                    WHEN 4
                       THEN 'WO'
                    WHEN 5
                       THEN 'SR'
                    WHEN 6
                       THEN 'RDM'
                    WHEN 7
                       THEN 'RR'
                    ELSE ''
                 END AS systemCode
      FROM open_pm.cr_created_from_other_sys ccfoss
                  LEFT JOIN one_tm.problems ps
               ON ps.problem_id = ccfoss.object_id
                  LEFT JOIN one_tm.troubles ts
               ON ts.trouble_id = ccfoss.object_id
                  LEFT JOIN open_pm.mr mr
               ON mr.mr_id = ccfoss.object_id
                  LEFT JOIN wfm.wo wo
               ON wo.wo_id = ccfoss.object_id
                  LEFT JOIN open_pm.sr sr
               ON sr.SR_ID = ccfoss.object_id
                  LEFT JOIN wfm.risk rk
               ON rk.RISK_ID          = ccfoss.object_id
      WHERE ccfoss.is_active = 1
        AND ccfoss.cr_id       = :cr_id
     ) sq
