select b.id, b.TYPE_ID as typeId, b.LOCATION_ID as locationId, b.UNIT_ID as unitId, c.UNIT_NAME || ' (' || c.UNIT_CODE || ')' unitName
           from common_gnoc.CFG_UNIT_TT_SPM b
           inner join common_gnoc.unit c on b.unit_id = c.unit_id
           where  b.TYPE_ID =:typeId and
              (select c.location_Id_Full from (SELECT a.LOCATION_ID , a.location_code ,
                              SYS_CONNECT_BY_PATH (a.LOCATION_ID, '/')|| '/' as location_Id_Full,
                             SYS_CONNECT_BY_PATH (a.location_name, ' / ')|| '/' as location_Name_Full,
                              LEVEL AS location_Level FROM common_gnoc.cat_location a  WHERE LEVEL < 6
                               START WITH a.parent_id IS NULL CONNECT BY PRIOR a.location_id = a.parent_id
                               ) c where c.location_id =:locationId )
                              like  '%/'||b.location_id || '/%'
