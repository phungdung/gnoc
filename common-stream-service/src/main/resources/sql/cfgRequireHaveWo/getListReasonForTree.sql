SELECT   a.id,
       a.reason_name reasonName,
       a.parent_id parentId,
       a.description,
       a.type_id typeId,
       a.reason_type reasonType,
       a.reason_code reasonCode,
       to_char(a.update_time,'dd/MM/yyyy HH24:MI:ss') updateTime,
       a.IS_CHECK_SCRIPT isCheckScript,
       a.is_update_closure isUpdateClosure
FROM   one_tm.cat_reason a
WHERE 1 = 1
