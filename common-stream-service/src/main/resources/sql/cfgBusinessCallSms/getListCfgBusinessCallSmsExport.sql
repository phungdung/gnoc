SELECT b.id id ,
  b.cfg_type_id cfgTypeId ,
  b.cd_id cdId ,
  i.item_name configType ,
  g.wo_group_name cdName ,
  b.cfg_level cfgLevel ,
  bu.user_id userId ,
  u.username username ,
  u.fullname fullName ,
  u.mobile mobileNumber
FROM wfm.cfg_business_call_sms b
INNER JOIN common_gnoc.cat_item i
ON b.cfg_type_id = i.item_id
INNER JOIN wfm.wo_cd_group g
ON b.cd_id = g.wo_group_id
INNER JOIN wfm.cfg_business_call_sms_user bu
ON b.id = bu.cfg_business_id
INNER JOIN common_gnoc.users u
ON b.id          = bu.cfg_business_id
WHERE bu.user_id = u.user_id
AND 1            =1
