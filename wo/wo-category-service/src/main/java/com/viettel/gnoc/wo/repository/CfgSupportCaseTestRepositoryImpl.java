package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.model.CfgSupportCaseTestEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgSupportCaseTestRepositoryImpl extends BaseRepository implements
    CfgSupportCaseTestRepository {

  @Override
  public ResultInSideDto add(CfgSupportCaseTestDTO cfgSupportCaseTestDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CfgSupportCaseTestEntity odTypeDetailEntity = getEntityManager()
        .merge(cfgSupportCaseTestDTO.toEntity());
    resultInSideDTO.setId(odTypeDetailEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(CfgSupportCaseTestDTO cfgSupportCaseTestDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(cfgSupportCaseTestDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CfgSupportCaseTestEntity odTypeEntity = getEntityManager()
        .find(CfgSupportCaseTestEntity.class, id);
    getEntityManager().remove(odTypeEntity);
    return resultInSideDTO;
  }

  @Override
  public CfgSupportCaseTestDTO checkCfgSupportCaseTestExist(Long cfgSuppportCaseId,
      String testCaseName, Long fileRequired) {
    List<CfgSupportCaseTestEntity> dataEntity = (List<CfgSupportCaseTestEntity>) findByMultilParam(
        CfgSupportCaseTestEntity.class,
        "cfgSuppportCaseId", cfgSuppportCaseId,
        "testCaseName", testCaseName,
        "fileRequired", fileRequired);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDto();
    }
    return null;
  }

  @Override
  public List<CfgSupportCaseTestDTO> getListCfgSupportCaseTestId(Long cfgSuppportCaseId) {
    List<CfgSupportCaseTestDTO> list = new ArrayList<>();
    List<CfgSupportCaseTestEntity> dataEntity = findByMultilParam(CfgSupportCaseTestEntity.class,
        "cfgSuppportCaseId", cfgSuppportCaseId);
    if (dataEntity != null && dataEntity.size() > 0) {
      for (CfgSupportCaseTestEntity caseTestEntity : dataEntity) {
        CfgSupportCaseTestDTO caseTestDTO = caseTestEntity.toDto();
        list.add(caseTestDTO);
      }
    }
    return list;
  }


}
