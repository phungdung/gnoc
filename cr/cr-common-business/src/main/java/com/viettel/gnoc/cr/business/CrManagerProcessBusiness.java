package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.List;

public interface CrManagerProcessBusiness {

  List<CrProcessDTO> getAllCrProcess (Long parentId);

}
