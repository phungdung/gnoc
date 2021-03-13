select  w.wo_id woId,
        w.wo_code woCode,
        w.need_support isNeedSupport,
        w.wo_content woContent,
        t.wo_type_name woTypeName,
        t.wo_type_code woTypeCode,
        case
          when wd.cc_priority_code is not null then to_char(wd.cc_priority_code)
          else p.priority_name
        end priorityName,
        w.priority_id priorityId,
        w.wo_system woSystem,
        w.wo_system_id woSystemId,
        w.cd_id cdId,
        w.ft_id ftId,
        w.plan_Code planCode,
        DECODE(w.status,
          0, :unassigned,
          1, :assigned,
          2, :reject,
          3, :dispatch,
          4, :accept,
          5, :inProcess,
          6, :closedFt,
          8, :closedCd,
          9, :pending)
          status,
        DECODE(w.result, 0, 'NOK', 1 ,'OK', '') result,
        cp.fullname || ' ('|| cp.username ||')' createPersonName,
        cp.email createPeronEmail,
        cp.mobile createPersonMobile,
        g.wo_group_name woGroupName,
        g.email woGroupEmail,
        g.mobile woGroupMobile,
        w.status comments,
        to_char(w.create_date + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') createDate,
        to_char(w.start_time + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') startTime,
        to_char(w.end_time + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') endTime,
        w.file_name fileName,
        w.wo_description woDescription,
        w.wo_type_id woTypeId,
        wd.account_isdn accountIsdn,
        wd.service_id serviceId,
        wd.infra_type infraType,
        DECODE(wd.infra_type, 1, 'CCN', 2 ,'CATV',3,'FCN',4,'GPON', null) infraTypeName,
        wd.cc_service_id ccServiceId,
        wd.cc_group_id ccGroupId,
        wd.is_cc_result isCcResult,
        wd.cc_result ccResult,
        wd.check_qos_internet_result checkQosInternetResult,
        wd.check_qos_th_result checkQosTHResult,
        wd.check_qr_code checkQrCode,
        to_char(w.end_pending_time + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') endPendingTime,
        pd.reason_pending_name reasonName,
        pd.reason_pending_id reasonId,
        pd.customer customer,
        pd.cus_phone phone,
        w.station_code stationCode,
        w.device_code deviceCode,
        ft.username ftName,
        wti.kedb_code kedbCode,
        wti.kedb_id kedbId,
        wti.required_tt_reason requiredTtReason,
        wti.able_mop ableMop,
        wki.contract_code contractCode,
        w.warehouse_code warehouseCode,
        w.construction_code constructionCode,
        wd.NUM_COMPLAINT numComplaint,
        wd.CUSTOMER_PHONE customerPhone,
        wti.LINK_CODE linkCode,
        wti.LINK_ID linkId,
        wti.ALARM_ID alarmId,
        w.ami_one_id amiOneId,
        w.distance distance,
        w.cable_Code cableCode,
        w.cable_Type_Code cableTypeCode,
        to_char(w.CUSTOMER_TIME_DESIRE_FROM + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') customerTimeDesireFrom,
        to_char(w.CUSTOMER_TIME_DESIRE_TO + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') customerTimeDesireTo,
        wd.PORT_CORRECT_ID portCorrectId,
        wd.ERR_TYPE_NIMS errTypeNims,
        wd.SUBSCRIPTION_ID subscriptionId,
        w.NUM_RECHECK numRecheck,
        w.DISTRICT_RECHECK districtRecheck,
        w.LOCATION_CODE locationCode,
        w.POINT_OK pointOk,
        w.DEVICE_TYPE deviceType,
        w.DEVICE_TYPE_NAME deviceTypeName,
        w.FAILURE_REASON failureReason,
        wd.CUSTOMER_NAME customerName,
        to_char(w.START_WORKING_TIME + :offset * interval '1' hour,'DD/MM/YYYY HH24:Mi:SS') startWorkingTime
from    wo w
        left join wo_detail wd on w.wo_id = wd.wo_id
        left join wo_pending pd on w.wo_id = pd.wo_id
        left join wo_type t on w.wo_type_id = t.wo_type_id
        left join common_gnoc.users cp on w.create_person_id = cp.user_id
        left join common_gnoc.users ft on w.ft_id = ft.user_id
        left join wo_cd_group g on w.cd_id = g.wo_group_id
        left join wo_priority p on w.priority_id = p.priority_id
        left join wo_trouble_info wti on w.wo_id = wti.wo_id
        left join wo_ktts_info wki on  w.wo_id = wki.wo_id
where   1 = 1
and (
             case when (select count(k.wo_pending_id) from wo_pending k where k.wo_id = w.wo_id) > 0
             then
                    case when pd.wo_pending_id = (select max(wo_pending_id) from wo_pending where wo_id = w.wo_id)
                         then 1 else 0 end
                    else 1 end
             ) = 1
