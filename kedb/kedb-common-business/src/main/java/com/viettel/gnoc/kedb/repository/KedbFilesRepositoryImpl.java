package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import com.viettel.gnoc.kedb.model.KedbFilesEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class KedbFilesRepositoryImpl extends BaseRepository implements KedbFilesRepository {

  @Override
  public List<KedbFilesDTO> onSearch(
      com.viettel.gnoc.ws.dto.KedbFilesDTO kedbFilesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(KedbFilesEntity.class,
        kedbFilesDTO.toInsideDTO(),
        rowStart, maxRow, sortType, sortFieldList);
  }
}
