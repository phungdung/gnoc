SELECT * FROM (
SELECT a.DEPARTMENT_ID key,
  a.DEPARTMENT_ID value,
  a.DEPT_CODE deptCode,
  a.DEPT_NAME title,
  INSTRB(a.DEPT_NAME,'(hết hiệu lực)') sub,
   CONNECT_BY_ISLEAF isleaf,
   0 disabled
FROM COMMON_GNOC.DEPARTMENT_VSA a
where STATUS = 1
  and ((:p_parent is not null and LEVEL = 2) or (:p_parent is null and PARENT_ID is null))
start with (:p_parent is null and PARENT_ID is null) or (:p_parent is not null and DEPARTMENT_ID = :p_parent)
connect by prior DEPARTMENT_ID = PARENT_ID
)
WHERE sub = '0'
