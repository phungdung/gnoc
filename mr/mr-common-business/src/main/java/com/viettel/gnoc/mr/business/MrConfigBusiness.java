package com.viettel.gnoc.mr.business;


import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import java.util.List;

/**
 * @author Dunglv3
 */
public interface MrConfigBusiness {

  List<MrConfigDTO> getConfigByGroup(String configGroup);
}
