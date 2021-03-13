package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleBtsHisRepository {

  List<MrScheduleBtsHisDetailDTO> getListWoBts(String woCode);

}
