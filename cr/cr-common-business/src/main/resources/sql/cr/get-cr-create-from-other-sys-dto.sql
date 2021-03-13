SELECT a.CCFOSM_ID ccfosmId ,
       a.CR_ID crId ,
       a.OBJECT_ID objectId ,
       a.STEP_ID stepId ,
       a.SYSTEM_ID systemId ,
       a.IS_ACTIVE isActive
FROM open_pm.cr_created_from_other_sys a
WHERE a.CR_ID =:crId
