select
  l.id id,
  (a.location_name||' ('||a.location_code||')') locationName,
  c.wo_group_name cdName,
  l.number_account_sc numberAccountSc,
  l.number_district_sc numberDistrictSc,
  l.number_account_tk numberAccountTk,
  l.number_district_tk numberDistrictTk,
  l.number_account_sc numberAccountScExp,
  l.number_district_sc numberDistrictScExp,
  l.number_account_tk numberAccountTkExp,
  l.number_district_tk numberDistrictTkExp
from
  common_gnoc.cat_location a,
  wfm.wo_cd_group c,
  wfm.map_provice_cd l
where
  l.cd_id = c.wo_group_id and
  l.location_code = a.location_code
