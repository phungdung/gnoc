package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import java.io.File;
import java.util.List;

public interface TransitionStateConfigBusiness {

  List<TransitionStateConfigDTO> onSearch(TransitionStateConfigDTO dto);

  Datatable getListTransitionStateConfigDTO(TransitionStateConfigDTO transitionStateConfigDTO);

  ResultInSideDto insertTransitionStateConfig(TransitionStateConfigDTO transitionStateConfigDTO);

  ResultInSideDto updateTransitionStateConfig(TransitionStateConfigDTO transitionStateConfigDTO);

  ResultInSideDto deleteTransitionStateConfig(Long id);

  TransitionStateConfigDTO findTransitionStateConfigById(Long id);

  File exportData(TransitionStateConfigDTO transitionStateConfigDTO) throws Exception;

  List<CatItemDTO> getListProcess();

  List<CatItemDTO> getListState(Long process);
}
