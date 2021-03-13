SELECT *
FROM
     ( WITH list_language_exchange AS
     (SELECT
             cat.ITEM_VALUE,
             cat.ITEM_NAME,
             LE.BUSSINESS_ID,
             LE.LEE_VALUE,
             LE.LEE_LOCALE
      FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
                                                                   Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE= 'PT_TYPE' and EDITABLE = 1)
           ) cat
             LEFT JOIN  COMMON_GNOC.LANGUAGE_EXCHANGE LE
               ON LE.BUSSINESS_ID = cat.ITEM_ID
                    and LE.APPLIED_SYSTEM = 1
                    and LE.APPLIED_BUSSINESS = 3
                    and LE.LEE_LOCALE = :p_leeLocale
     ),
         list_search AS
       (SELECT a.ID id,
               a.TYPE_ID typeId,
               a.TYPE_UNIT typeUnit,
               a.LOCATION_ID locationId,
               b.LOCATION_NAME locationName,
               (c.UNIT_NAME
                  ||'('
                  || c.UNIT_CODE
                  ||')') unitName,
               d.ITEM_NAME typeName ,
               CASE a.TYPE_UNIT
                 WHEN 1
                         THEN 'Cty Công trình'
                 WHEN 2
                         THEN 'TKTU tỉnh'
                 ELSE ''
                   END typeUnitName,
               a.UNIT_ID
        FROM COMMON_GNOC.CFG_UNIT_TT_SPM a
               LEFT JOIN COMMON_GNOC.CAT_ITEM d
                 ON a.TYPE_ID=d.ITEM_ID
               LEFT JOIN COMMON_GNOC.CAT_LOCATION b
                 ON a.LOCATION_ID=b.LOCATION_ID
               LEFT JOIN COMMON_GNOC.UNIT c
                 ON a.UNIT_ID=c.UNIT_ID
        WHERE 1     =1
       ),
         list_result1 AS
       (SELECT a.id ,
               a.typeId,
               a.typeUnit,
               a.locationId,
               a.locationName,
               a.unitName,
               a.typeUnitName,
               a.UNIT_ID,
               CASE
                 WHEN llx.LEE_VALUE IS NULL
                         THEN a.typeName
                 ELSE llx.LEE_VALUE
                   END typeName
        FROM list_search a
               LEFT JOIN list_language_exchange llx
                 ON a.typeId = llx.BUSSINESS_ID
       )
     SELECT c.id ,
            c.typeId,
            c.typeUnit,
            c.locationId,
            c.locationName,
            c.unitName,
            c.typeUnitName,
            c.typeName,
            c.UNIT_ID unitId
     FROM list_result1 c
     ) a
WHERE 1=1
