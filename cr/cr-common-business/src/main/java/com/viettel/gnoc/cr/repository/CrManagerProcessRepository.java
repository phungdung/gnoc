package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrManagerProcessRepository {


  List<CrProcessDTO> getAllCrProcess(Long parentId);
}
