package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import com.viettel.gnoc.kedb.model.KedbRatingEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface KedbRatingRepository {

  Datatable getListKedbRatingDTO(KedbRatingInsideDTO kedbRatingInsideDTO);

  KedbRatingInsideDTO findKedbRatingById(Long id);

  KedbRatingInsideDTO getKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  ResultInSideDto insertKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  ResultInSideDto updateKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  List<KedbRatingEntity> findKedbRatingEntityByMultilParam(KedbRatingInsideDTO kedbRatingInsideDTO);

  ResultInSideDto insertOrUpdateListKedbRating(List<KedbRatingInsideDTO> listKedbRatingInsideDTO);

  ResultInSideDto deleteKedbRating(Long id);

  List<String> getSequenseKedbRating(String seqName, int... size);

  List<KedbRatingInsideDTO> getListKedbRatingByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
