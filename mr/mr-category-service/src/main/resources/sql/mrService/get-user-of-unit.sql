SELECT
				 a.USERNAME username
				,a.UNIT_ID unitId
				,a.USER_ID userId
				,b.UNIT_NAME unitName
				,r.ROLE_CODE roleCode
				,r.ROLE_NAME roleName
				 FROM COMMON_GNOC.users a
				 INNER JOIN COMMON_GNOC.unit b ON a.UNIT_ID = b.UNIT_ID
				 LEFT JOIN (
				 SELECT rs.ROLE_ID, rs.ROLE_CODE, rs.ROLE_NAME, rr.USER_ID FROM COMMON_GNOC.ROLES rs INNER JOIN COMMON_GNOC.ROLE_USER rr ON rs.ROLE_ID = rr.ROLE_ID
				 ) r ON a.USER_ID = r.USER_ID
				 WHERE a.UNIT_ID = :unitId
