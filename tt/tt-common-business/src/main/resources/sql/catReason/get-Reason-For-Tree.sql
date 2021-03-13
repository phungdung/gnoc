SELECT a.id,
       a.reason_name reasonName,
       a.parent_id parentId,
       a.description,
       a.type_id typeId,
       a.reason_type reasonType,
       a.reason_code reasonCode,
       a.update_time updateTime,
       a.IS_CHECK_SCRIPT isCheckScript,
       a.is_update_closure isUpdateClosure
FROM   cat_reason a
WHERE 1 = 1
