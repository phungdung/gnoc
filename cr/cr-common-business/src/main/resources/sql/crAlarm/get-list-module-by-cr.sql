select cmd_id cmdId ,cr_id crId, service_code serviceCode, service_name serviceName,nation_code nationCode, TO_CHAR(create_time, 'dd/mm/yyyy hh24:mi:ss') createTime, module_code moduleCode, module_name moduleName From CR_MODULE_DETAIL cr
where 1=1
