package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.model.SRCreatedFromOtherSysEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRCreatedFromOtherSysRepositoryImpl extends BaseRepository implements
    SRCreatedFromOtherSysRepository {

  @Override
  public List<SRCreatedFromOtherSysDTO> getListSRCreatedFromOtherSysDTO(
      SRCreatedFromOtherSysDTO dto) {
    List<SRCreatedFromOtherSysEntity> lstEntity = findByMultilParam(
        SRCreatedFromOtherSysEntity.class
        , "srCode", dto.getSrCode()
        , "systemName", dto.getSystemName());
    List<SRCreatedFromOtherSysDTO> lst = new ArrayList<>();
    if (lstEntity != null && !lstEntity.isEmpty()) {
      for (SRCreatedFromOtherSysEntity item : lstEntity) {
        if (item.toDTO() != null) {
          lst.add(item.toDTO());
        }
      }
    }
    return lst;
  }

  @Override
  public ResultInSideDto insertSRCreateFromOtherSys(SRCreatedFromOtherSysDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      if (dto != null) {
        SRCreatedFromOtherSysEntity model = dto.toEntity();
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO OPEN_PM.SR_CREATED_FROM_OTHER_SYS "
            + " ( SYSTEM"
            + ",SUB_ORDER_ID"
            + ",SERVICE_NAME"
            + ",CONTENT"
            + ",CUSTOMER"
            + ",ADDRESS"
            + ",ACCOUNT_SIP_TRUNK"
            + ",IP_PBX"
            + ",IP_PROXY"
            + ",SUBSCRIBERS"
            + ",CALLS"
            + ",SR_CODE"
            + ",SYSTEM_NAME"
            + ",OBJECT_ID"
            + ")"
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        );
        Query query = getEntityManager().createNativeQuery(sql.toString());
        query.setParameter(1, model.getSystem() == null ? "" : model.getSystem());
        query.setParameter(2, model.getSubOrderId() == null ? "" : model.getSubOrderId());
        query.setParameter(3, model.getServiceName());
        query.setParameter(4, model.getContent());
        query.setParameter(5, model.getCustomer());
        query.setParameter(6, model.getAddress());
        query.setParameter(7, model.getAccountSipTrunk());
        query.setParameter(8, model.getIpPbx());
        query.setParameter(9, model.getIpProxy());
        query.setParameter(10, model.getSubscribers());
        query.setParameter(11, model.getCalls() == null ? "" : model.getCalls());
        query.setParameter(12, model.getSrCode());
        query.setParameter(13, model.getSystemName());
        query.setParameter(14, model.getObjectId() == null ? "" : model.getObjectId());
        int result = query.executeUpdate();
        if (result > 0) {
          resultInSideDto.setKey(Constants.RESULT.SUCCESS);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }
}
