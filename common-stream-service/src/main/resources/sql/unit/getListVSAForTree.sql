SELECT a.department_id unitId,
  a.dept_name unitName,
  a.dept_code unitCode,
  a.parent_id parentUnitId,
  a.status,
  a.description,
  a.is_committee isCommittee
FROM COMMON_GNOC.DEPARTMENT_VSA a
WHERE 1=1
