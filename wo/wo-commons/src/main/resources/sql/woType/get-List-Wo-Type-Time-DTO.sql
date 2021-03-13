SELECT  a.wo_type_time_id woTypeTimeId,
        a.wo_type_id woTypeId,
        a.duration duration,
        a.is_immediate isImmediate,
        a.user_appove_pending userApprovePending,
        a.wait_for_appove_pending waitForApprovePending
FROM    wo_type_time a
WHERE   1 = 1
