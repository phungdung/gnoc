select  a.cfg_support_case_id cfgSupportCaseID,
        b.case_name caseName,
        a.cfg_support_case_test_id testCaseId,
        c.test_case_name testCaseName,
        a.result result,
        a.description description,
        a.file_name fileName
from    wo_support a,
        cfg_support_case b,
        cfg_support_case_test c
where   a.cfg_support_case_id = b.id
and     a.cfg_support_case_test_id = c.id
and     a.wo_id = :woId
