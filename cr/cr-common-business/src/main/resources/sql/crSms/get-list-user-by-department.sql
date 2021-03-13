 select uS.user_id as userId, 
       us.username as userName, 
       us.email as email, 
       us.mobile as cellPhone, 
       us.unit_id as deptId,
       us.user_language as userLanguage, 
       ut.sms_gateway_id as smsGatewayId, 
       (select item_value from common_gnoc.cat_item where 
       item_code='CR_ALIAS') as alias 
       from common_gnoc.users us 
       left join common_gnoc.unit ut on 
       ut.unit_id = us.unit_id 

       where (us.IS_NOT_RECEIVE_MESSAGE is null or us.IS_NOT_RECEIVE_MESSAGE<>1) 
       and us.is_enable = 1 
       and ut.status = 1 
