select a.CAS_ID casId , a.TYPE type , a.NATION_CODE nationCode , a.VENDOR_CODE vendorCode ,
                     a.VENDOR_NAME vendorName , a.MODULE_CODE moduleCode , a.MODULE_NAME moduleName , a.APPROVAL_STATUS approvalStatus ,
                     a.APPROVAL_USER_ID approvalUserId , a.FAULT_SRC faultSrc , a.FAULT_ID faultId , a.FAULT_NAME faultName ,
                     a.FAULT_GROUP_NAME  faultGroupName , a.FAULT_LEVEL_CODE faultLevelCode , a.DEVICE_TYPE_CODE deviceTypeCode ,
                     b.CR_PROCESS_NAME crProcessName, a.keyword, a.NUMBER_OCCURENCES numberOccurences
               from CR_ALARM_SETTTING a
               left join CR_PROCESS b on a.CR_PROCESS_ID = b.CR_PROCESS_ID
                where  a.APPROVAL_STATUS = 1
