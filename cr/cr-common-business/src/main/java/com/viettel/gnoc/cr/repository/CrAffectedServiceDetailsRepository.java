package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrAffectedServiceDetailsRepository {

  void deleteAffSerByCrId(String crId);

  String saveListDTONoIdSession(List<CrAffectedServiceDetailsDTO> obj);

  List<CrAffectedServiceDetailsDTO> search(CrAffectedServiceDetailsDTO serviceDetailsDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
