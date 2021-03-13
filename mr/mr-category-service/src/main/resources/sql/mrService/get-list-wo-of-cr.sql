      SELECT
           WO_ID woId
          ,CD_ID cdId
          ,WO_SYSTEM woSystem
          ,WO_CODE woCode
          ,CREATE_PERSON_ID createPersonId
          ,WO_TYPE_ID woTypeId
          ,PRIORITY_ID priorityId
          ,WO_CONTENT woContent
          ,CD_ID cdId
           FROM WFM.WO WHERE WO_SYSTEM_ID = :wo_system_id
