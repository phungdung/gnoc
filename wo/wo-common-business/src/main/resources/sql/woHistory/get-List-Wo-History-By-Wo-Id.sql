SELECT    h.wo_history_id woHistoryId,
          h.old_status oldStatus,
          h.new_status newStatus,
          h.wo_id woId,
          h.wo_code woCode,
          h.wo_content woContent,
          h.user_id userId,
          h.user_name userName,
          h.file_name fileName,
          h.comments comments,
          h.update_time + :offset * interval '1' hour updateTime,
          h.create_message createMessage,
          h.create_person_id createPersonId,
          h.cd_id cdId,
          h.ft_Id ftId,
          h.is_send_ibm isSendIbm,
          h.nation nation
FROM      wo_history h
WHERE     h.wo_id = :woId
ORDER BY  h.update_time DESC
