SELECT ccfosm_id ccfosmId,
       cr_id crId,
       system_id systemId,
       object_id objectId,
       step_id stepId,
       is_active isActive
FROM open_pm.cr_created_from_other_sys a
WHERE a.cr_id   = :crId
  AND a.system_id = :systemId
