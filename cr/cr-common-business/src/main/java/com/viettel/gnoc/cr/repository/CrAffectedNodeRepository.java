package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrAffectedNodeRepository {

  void deleteAffectedNodeByCrId(String crId, Date insertTime);

  String saveListDTONoIdSession(List<CrAffectedNodesDTO> obj, Date createDate);
}
