update WO_AUTO_CHECK set  status = :status,
                          UPDATED_TIME = sysdate,
                          result_description = :resultDescription
where wo_id = :woId
