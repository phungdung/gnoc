WITH
     list_language_exchange AS (
    select
           LE.LEE_ID,
           LE.APPLIED_SYSTEM,
           LE.APPLIED_BUSSINESS,
           LE.BUSSINESS_ID,
           LE.BUSSINESS_CODE,
           LE.LEE_LOCALE,
           LE.LEE_VALUE
    from COMMON_GNOC.LANGUAGE_EXCHANGE LE
           join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = 'COMMON_GNOC') CAT1
             on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
           join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = 'COMMON_GNOC.CFG_CHILD_ARRAY') CAT2
             on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
    where LE.LEE_LOCALE = :p_leeLocale
    ),
     list_result AS (
    Select
           c.CHILDREN_ID
        , c.CHILDREN_CODE
        ,case
           when llx.LEE_VALUE is null
                   then TO_CHAR(c.CHILDREN_NAME)
           else TO_CHAR(llx.LEE_VALUE) end CHILDREN_NAME
        , c.STATUS
        , c.UPDATED_TIME
        , c.UPDATED_USER
        , c.PARENT_ID
    from COMMON_GNOC.CFG_CHILD_ARRAY c
           left join list_language_exchange llx on c.CHILDREN_ID = llx.BUSSINESS_ID
    )
select
       cr.CHILDREN_ID childrenId,
       cr.CHILDREN_CODE childrenCode,
       cr.CHILDREN_NAME childrenName,
       cr.STATUS status,
       cr.UPDATED_TIME updatedTime,
       cr.UPDATED_USER updatedUser,
       cr.PARENT_ID parentId
from list_result cr
where 1=1
