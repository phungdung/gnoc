select us.user_id as userId,
      us.username as userName,
      us.email as email,
      us.mobile as cellPhone,
      us.unit_id as deptId,
      us.user_language as userLanguage,
      ut.sms_gateway_id as smsGatewayId,
      (select item_value from common_gnoc.cat_item where
      item_code='CR_ALIAS') as alias,
      vuae.path AS deptCode
      FROM common_gnoc.users us
      LEFT JOIN common_gnoc.v_user_role vure
      ON us.user_id = vure.user_id
      LEFT JOIN common_gnoc.v_unit_as_tree vuae
      ON vure.unit_id = vuae.unit_id
      LEFT JOIN common_gnoc.unit ut
      ON ut.unit_id = vuae.unit_id WHERE is_manager = 1
      AND vuae.unit_id IN (SELECT unit_id FROM common_gnoc.unit ut
      START WITH unit_id = :unit_id
      CONNECT BY PRIOR  parent_unit_id =unit_id)
      ORDER BY PATH desc
