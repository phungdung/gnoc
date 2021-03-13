SELECT
  prc.cr_process_id AS valueStr,
  prc.tree               AS displayStr,
  prc.impact_type || ',' || prc.isleaf AS secondValue,
  thirdValue
FROM
  (SELECT cps.cr_process_id,
    cps.cr_process_name,
    cps.cr_process_code,
    parent_id,
    RPAD('--|', (level-1)*3, '--|') || cr_process_name AS tree,
    level,
    is_active,
    CONNECT_BY_ROOT cr_process_id                       AS root_id,
    LTRIM(SYS_CONNECT_BY_PATH(cr_process_id, '-'), '-') AS path,
    CONNECT_BY_ISLEAF                                   AS isLEAF,
    cps.impact_type ,
    cps.RISK_LEVEL AS thirdValue
  FROM open_pm.cr_process cps
    START WITH ( cps.parent_id IS NULL AND is_active = 1 )
    CONNECT BY parent_id = PRIOR cr_process_id
  ORDER SIBLINGS BY cr_process_id
  ) prc
WHERE prc.cr_process_id IN
  (SELECT cps.cr_process_id
  FROM open_pm.cr_process cps
    START WITH ( 1, cps.cr_process_id ) IN (
