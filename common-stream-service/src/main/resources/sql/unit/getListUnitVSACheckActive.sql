SELECT * FROM(SELECT ut.department_id unitId,
  ut.dept_code unitCode,
  ut.parent_id,
  CASE
    WHEN ut.dept_code IS NULL
    THEN ''
    ELSE TO_CHAR(ut.dept_code
      || ' ('
      || ut.dept_name
      || ')')
  END AS unitName,
INSTRB(ut.dept_name,'(hết hiệu lực)') statusStr
FROM common_gnoc.department_vsa ut
LEFT JOIN common_gnoc.department_vsa parentUt
ON ut.parent_id = parentUt.department_id
WHERE ut.status = 1)
