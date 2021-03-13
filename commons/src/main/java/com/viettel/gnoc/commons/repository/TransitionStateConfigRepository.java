package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitionStateConfigRepository {

  List<TransitionStateConfigDTO> onSearch(TransitionStateConfigDTO dto);

  Datatable getListTransitionStateConfigDTO(TransitionStateConfigDTO transitionStateConfigDTO);

  ResultInSideDto insertTransitionStateConfig(TransitionStateConfigDTO transitionStateConfigDTO);

  ResultInSideDto updateTransitionStateConfig(TransitionStateConfigDTO transitionStateConfigDTO);

  ResultInSideDto deleteTransitionStateConfig(Long id);

  TransitionStateConfigDTO findTransitionStateConfigById(Long id);

  List<CatItemDTO> getListProcess();

  List<CatItemDTO> getListState(Long process);

  List<TransitionStateConfigDTO> getListTransitionStateConfigExport(
      TransitionStateConfigDTO transitionStateConfigDTO);
}
