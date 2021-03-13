SELECT pr.problem_id problemId,
  pr.problem_code problemCode,
  pr.problem_name problemName,
  pr.description description,
  pr.type_id typeId,
  pr.sub_category_id subCategoryId,
  pr.notes notes,
  pr.priority_id priorityId,
  pr.impact_id impactId,
  pr.urgency_id urgencyId,
  pr.problem_state problemState,
  pr.access_id accessId,
  pr.affected_node affectedNode,
  pr.vendor vendor,
  pr.affected_service affectedService,
  pr.location location,
  pr.location_id locationId,
  pr.created_time     + :offset * interval '1' hour createdTime,
  pr.last_update_time + :offset * interval '1' hour lastUpdateTime,
  pr.assign_time_temp + :offset * interval '1' hour assignTimeTemp,
  pr.es_rca_time      + :offset * interval '1' hour esRcaTime,
  pr.es_wa_time       + :offset * interval '1' hour esWaTime,
  pr.es_sl_time       + :offset * interval '1' hour esSlTime,
  pr.started_time     + :offset * interval '1' hour startedTime,
  pr.ended_time       + :offset * interval '1' hour endedTime,
  pr.create_user_id createUserId,
  pr.create_unit_id createUnitId,
  pr.create_user_name createUserName,
  pr.create_unit_name createUnitName,
  pr.create_user_phone createUserPhone,
  pr.deferred_time + :offset * interval '1' hour deferredTime,
  pr.insert_source insertSource,
  pr.is_send_message isSendMessage,
  pr.related_tt relatedTt,
  pr.related_pt relatedPt,
  pr.related_kedb relatedKedb,
  pr.pt_type ptType,
  pr.rca_found_time + :offset * interval '1' hour rcaFoundTime,
  pr.wa_found_time  + :offset * interval '1' hour waFoundTime,
  pr.sl_found_time  + :offset * interval '1' hour slFoundTime,
  pr.closed_time    + :offset * interval '1' hour closedTime,
  pr.rca rca,
  pr.wa wa,
  pr.solution solution,
  pr.solution_type solutionType,
  pr.receive_unit_id receiveUnitId,
  pr.receive_user_id receiveUserId,
  pr.time_used timeUsed,
  pr.worklog worklog,
  pr.influence_scope influenceScope,
  pr.delay_time + :offset * interval '1' hour delayTime,
  pr.rca_type rcaType,
  pr.categorization categorization,
  pr.state_code stateCode,
  pr.pt_related_type ptRelatedType,
  pr.contact_info contactInfo,
  pr.pm_group pmGroup,
  pr.close_code closeCode,
  pr.process process,
  pr.assigned_time + :offset * interval '1' hour assignedTime,
  pr.pm_id pmId,
  pr.pm_user_name pmUserName,
  pr.SOFTWARE_VERSION softwareVersion,
  pr.HARDWARE_VERSION hardwareVersion,
  pr.pt_duplicate ptDuplicate,
  pr.reason_overdue reasonOverdue,
  pr.is_chat isChat,
  pr.colorCheck colorCheck
FROM
  (SELECT pr2.*,
    CASE
      WHEN pr2.colorCheck1 IS NOT NULL AND pr2.colorCheck2  IS NULL AND pr2.colorCheck3 IS NULL
      THEN pr2.colorCheck1
      WHEN pr2.colorCheck1 IS NOT NULL AND pr2.colorCheck2  IS NOT NULL AND pr2.colorCheck3 IS NULL
      THEN pr2.colorCheck1
      WHEN pr2.colorCheck1 IS NOT NULL AND pr2.colorCheck2  IS NULL AND pr2.colorCheck3 IS NULL
      THEN pr2.colorCheck1
      WHEN pr2.colorCheck1 IS NOT NULL AND pr2.colorCheck2  IS NOT NULL AND pr2.colorCheck3 IS NOT NULL
      THEN pr2.colorCheck1
      WHEN pr2.colorCheck1 IS NULL AND pr2.colorCheck2  IS NOT NULL AND pr2.colorCheck3 IS NOT NULL
      THEN pr2.colorCheck2
      WHEN pr2.colorCheck1 IS NULL AND pr2.colorCheck2  IS NOT NULL AND pr2.colorCheck3 IS NULL
      THEN pr2.colorCheck2
      WHEN pr2.colorCheck1 IS NULL AND pr2.colorCheck2  IS NULL AND pr2.colorCheck3  IS NOT NULL
      THEN pr2.colorCheck3
      WHEN pr2.colorCheck1 IS NULL AND pr2.colorCheck2  IS NULL AND pr2.colorCheck3  IS NULL
      THEN 'white'
    END AS colorCheck
  FROM
    (SELECT pr1.*,
      CASE
        WHEN (pr1.ES_RCA_TIME  IS NOT NULL
        AND pr1.RCA_FOUND_TIME IS NULL
        AND pr1.CREATED_TIME   IS NOT NULL)
        THEN
          CASE
            WHEN (pr1.ES_RCA_TIME - SYSDATE <= 0)
            THEN 'red'
            WHEN ((pr1.ES_RCA_TIME - SYSDATE)/(pr1.ES_RCA_TIME - pr1.CREATED_TIME) < 0.2)
            THEN 'yellow'
          END
      END AS colorCheck1,
      CASE
        WHEN (pr1.ES_WA_TIME  IS NOT NULL
        AND pr1.WA_FOUND_TIME IS NULL
        AND pr1.CREATED_TIME  IS NOT NULL)
        THEN
          CASE
            WHEN (pr1.ES_WA_TIME - SYSDATE <= 0)
            THEN 'red'
            WHEN ((pr1.ES_WA_TIME - SYSDATE)/(pr1.ES_WA_TIME - pr1.CREATED_TIME) < 0.2)
            THEN 'yellow'
          END
      END AS colorCheck2,
      CASE
        WHEN (pr1.ES_SL_TIME  IS NOT NULL
        AND pr1.SL_FOUND_TIME IS NULL
        AND pr1.CREATED_TIME  IS NOT NULL)
        THEN
          CASE
            WHEN (pr1.ES_SL_TIME - SYSDATE <= 0)
            THEN 'red'
            WHEN ((pr1.ES_SL_TIME - SYSDATE)/(pr1.ES_SL_TIME - pr1.CREATED_TIME) < 0.2)
            THEN 'yellow'
          END
      END AS colorCheck3
    FROM one_tm.Problems pr1
    ) pr2
  ) pr
WHERE 1  = 1
