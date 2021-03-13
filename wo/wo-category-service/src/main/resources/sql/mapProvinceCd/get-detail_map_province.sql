select
  l.id id,
  (a.location_name||' ('||a.location_code||')') locationName,
  a.location_code locationCode,
  c.wo_group_name cdName,
  l.CD_ID cdId,
  l.number_account_sc numberAccountSc,
  l.number_district_sc numberDistrictSc,
  l.number_account_tk numberAccountTk,
  l.number_district_tk numberDistrictTk
from
  common_gnoc.cat_location a,
  wfm.wo_cd_group c,
  wfm.map_provice_cd l
where
  l.cd_id = c.wo_group_id and
  l.location_code = a.location_code and
  l.id =:id

