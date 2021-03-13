package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrMobileRepository {

  List<CrDTO> getListCRBySearchTypeCount(CrDTO crDTO);

  ObjResponse getListCRBySearchTypePaggingMobile(CrDTO crDTO, int start, int maxResult,
      String locale);
  CrDTO getCrByIdExtends(String crId);
}
