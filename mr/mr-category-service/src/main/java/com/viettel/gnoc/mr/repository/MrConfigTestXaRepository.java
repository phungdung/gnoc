package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrConfigTestXaRepository {

  Datatable getListMrConfigTestXa(MrConfigTestXaDTO mrConfigTestXaDTO);

  ResultInSideDto insertOrUpdate(MrConfigTestXaDTO mrConfigTestXaDTO);

  ResultInSideDto deleteMrConfigTestXa(Long configId);

  MrConfigTestXaDTO getDetail(Long configId);

  List<MrConfigTestXaDTO> checkListDTOExisted(MrConfigTestXaDTO mrConfigTestXaDTO);

  List<MrConfigTestXaDTO> getListStation();

  List<MrConfigTestXaDTO> getDataExport(MrConfigTestXaDTO mrConfigTestXaDTO);

}
