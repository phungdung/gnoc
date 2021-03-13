package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdRelationRepository {

  ResultInSideDto insertOrUpdate(OdRelationDTO odDTO);

  List<OdRelationDTO> getRelationsByOdId(Long odId);
}
