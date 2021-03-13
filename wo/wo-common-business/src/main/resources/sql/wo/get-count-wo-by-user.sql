SELECT COUNT(woId) totalRow
FROM
  (SELECT w.wo_id woId ,
    w.wo_code woCode ,
    w.need_support isNeedSupport ,
    w.wo_content woContent ,
    t.wo_type_name woTypeName ,
    t.wo_type_code woTypeCode ,
    p.priority_name priorityName ,
    w.wo_system woSystem ,
    w.wo_system_id woSystemId ,
    w.cd_id cdId ,
    w.ft_id ftId ,
    w.plan_Code planCode ,
    DECODE(w.status, 0, 'UNASSIGNED', 1 , 'ASSIGNED', 2 , 'REJECT', 3 , 'DISPATCH', 4 , 'ACCEPT
', 5 , 'INPROCESS', 6 , 'CLOSED_FT
', 8 , 'CLOSED_CD', 9 , 'PENDING') status ,
    DECODE(w.result,0, 'NOK', 1 ,'OK', '') result ,
    cp.fullname
    || ' ('
    || cp.username
    ||')' createPersonName ,
    cp.mobile createPersonMobile ,
    g.wo_group_name woGroupName ,
    g.email woGroupEmail ,
    g.mobile woGroupMobile ,
    w.status comments ,
    w.file_name fileName ,
    w.wo_description woDescription ,
    w.wo_type_id woTypeId ,
    wd.account_isdn accountIsdn ,
    wd.service_id serviceId ,
    wd.infra_type infraType ,
    DECODE(wd.infra_type,1, 'CCN', 2 ,'CATV',3,'FCN',4,'GPON', NULL) infraTypeName ,
    wd.cc_service_id ccServiceId ,
    wd.cc_group_id ccGroupId ,
    wd.is_cc_result isCcResult ,
    wd.cc_result ccResult ,
    wd.check_qos_internet_result checkQosInternetResult ,
    wd.check_qos_th_result checkQosTHResult ,
    wd.check_qr_code checkQrCode ,
    pd.reason_pending_name reasonName ,
    pd.reason_pending_id reasonId ,
    pd.customer customer ,
    pd.cus_phone phone ,
    w.station_code stationCode ,
    w.device_code deviceCode ,
    ft.username ftName ,
    wti.kedb_code kedbCode ,
    wti.kedb_id kedbId ,
    wti.required_tt_reason requiredTtReason ,
    wti.able_mop ableMop ,
    wki.contract_code contractCode ,
    w.warehouse_code warehouseCode ,
    w.construction_code constructionCode ,
    wd.NUM_COMPLAINT numComplaint ,
    wd.CUSTOMER_PHONE customerPhone ,
    wti.LINK_CODE linkCode ,
    wti.LINK_ID linkId ,
    wti.ALARM_ID alarmId ,
    w.ami_one_id amiOneId ,
    w.distance ,
    w.cable_Code cableCode,
    w.cable_Type_Code cableTypeCode ,
    wd.PORT_CORRECT_ID portCorrectId ,
    wd.ERR_TYPE_NIMS errTypeNims ,
    wd.SUBSCRIPTION_ID subscriptionId ,
    w.NUM_RECHECK numRecheck,
    w.LOCATION_CODE locationCode,
    w.POINT_OK pointOk
  FROM wfm.wo w
  LEFT JOIN wo_detail wd
  ON w.wo_id = wd.wo_id
  LEFT JOIN wo_pending pd
  ON w.wo_id = pd.wo_id
  LEFT JOIN wo_type t
  ON w.wo_type_id = t.wo_type_id
  LEFT JOIN common_gnoc.users cp
  ON w.create_person_id = cp.user_id
  LEFT JOIN common_gnoc.users ft
  ON w.ft_id = ft.user_id
  LEFT JOIN wo_cd_group g
  ON w.cd_id = g.wo_group_id
  LEFT JOIN wo_priority p
  ON w.priority_id = p.priority_id
  LEFT JOIN wo_trouble_info wti
  ON w.wo_id = wti.wo_id
  LEFT JOIN wo_ktts_info wki
  ON w.wo_id = wki.wo_id
  WHERE 1    =1
  AND (
    CASE
      WHEN (SELECT COUNT(k.WO_PENDING_ID) FROM wo_pending k WHERE k.wo_id = w.wo_id) >0
      THEN
        CASE
          WHEN pd.WO_PENDING_ID =
            (SELECT MAX(WO_PENDING_ID) FROM wo_pending WHERE wo_id = w.wo_id
            )
          THEN 1
          ELSE 0
        END
      ELSE 1
    END ) = 1
