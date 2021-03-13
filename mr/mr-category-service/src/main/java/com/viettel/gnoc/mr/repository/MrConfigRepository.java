package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrConfigRepository {

  List<MrConfigDTO> getConfigByGroup(String configGroup);

}
