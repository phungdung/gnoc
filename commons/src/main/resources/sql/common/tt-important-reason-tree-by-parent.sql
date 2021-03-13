 select
  a.id key,
  a.id value,
  a.REASON_NAME title,
  CONNECT_BY_ISLEAF isleaf,
  0 disabled
FROM COMMON_GNOC.TROUBLE_REASON a
 WHERE 1 = 1
 AND ((:p_parent is not null and LEVEL = 2) or (:p_parent is null and a.PARENT_ID is null))
 AND (:typeId is null or to_char(a.type_id) = :typeId)
start with (:p_parent is null and a.PARENT_ID is null) or (:p_parent is not null and  to_char(a.ID) = :p_parent)
connect by prior a.ID = a.PARENT_ID
