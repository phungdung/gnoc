SELECT SYS_CONNECT_BY_PATH (a.CR_PROCESS_ID, ',')|| ',' as crProcessCode,
LEVEL AS crProcessId FROM open_pm.CR_PROCESS a  WHERE LEVEL < 6
and CR_PROCESS_ID = :crProcessId
START WITH a.parent_id IS NULL CONNECT BY PRIOR a.CR_PROCESS_ID = a.parent_id
