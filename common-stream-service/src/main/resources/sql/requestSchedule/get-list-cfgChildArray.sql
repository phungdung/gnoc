SELECT
       t1.CHILDREN_ID childrenId,
       t1.PARENT_ID parentId,
       t1.CHILDREN_CODE childrenCode,
       t1.CHILDREN_NAME childrenName,
       t1.STATUS status,
       t1.UPDATED_USER updatedUser,
       to_char(t1.UPDATED_TIME, 'dd/MM/yyyy HH24:mi:ss') updatedTime
FROM COMMON_GNOC.CFG_CHILD_ARRAY t1
WHERE 1 =1
