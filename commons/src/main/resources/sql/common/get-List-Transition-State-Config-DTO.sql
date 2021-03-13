SELECT  t.id,
        t.begin_state_id beginStateId,
        b.item_name beginStateName,
        b.item_code beginStateCode,
        t.end_state_id endStateId,
        e.item_name endStateName,
        e.item_code endStateCode,
        t.description,
        t.process,
        t.skip_status skipStatus,
        t.role_code roleCode,
        c.category_name processName,
        c.category_code processCode
FROM    common_gnoc.transition_state_config t
        LEFT JOIN common_gnoc.cat_item b ON t.begin_state_id = b.item_id
        LEFT JOIN common_gnoc.cat_item e ON t.end_state_id = e.item_id
        LEFT JOIN common_gnoc.category c ON t.process = c.category_id
WHERE   1 = 1
