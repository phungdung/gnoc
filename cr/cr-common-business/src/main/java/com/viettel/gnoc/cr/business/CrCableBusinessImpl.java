package com.viettel.gnoc.cr.business;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPort;
import com.viettel.gnoc.commons.incident.provider.WSNIMSInfraPortFactory;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.TempCableDTO;
import com.viettel.gnoc.cr.repository.CrCableRepository;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.nims.infra.webservice.InfraSleevesBO;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nims.infra.webservice.JsonResponseBO;
import com.viettel.nims.infra.webservice.ParameterBO;
import com.viettel.nims.infra.webservice.RequestInputBO;
import com.viettel.security.PassTranformer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class CrCableBusinessImpl implements CrCableBusiness {

  InfraWS port = null;
  @Autowired
  CrCableRepository crCableRepository;

  @Autowired
  WSNocprov4Port wsNocprov4Port;

  @Autowired
  WSNIMSInfraPort wsnimsInfraPort;

  @Autowired
  @Qualifier("WSNIMSInfraPortFactorys")
  WSNIMSInfraPortFactory wsnimsInfraPortFactory;

  @Value("${application.conf.user_name_nims_2}")
  private String username;

  @Value("${application.conf.password_nims_2}")
  private String password;

  private void createConnect() {
    try {
      port = (InfraWS) wsnimsInfraPortFactory.getWsFactory().borrowObject();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

  @Override
  public Datatable GNOC_getInfraCableLane(CrCableDTO crCableDTO) {
    List<CrCableDTO> res = null;
    Datatable datatable = new Datatable();
    int page = crCableDTO.getPage();
    int size = crCableDTO.getPageSize();
    size = (size > 0) ? size : 5;
    crCableDTO.setSortName(null);
    crCableDTO.setSortType(null);
    crCableDTO.setPage(1);
    crCableDTO.setPageSize(Integer.MAX_VALUE);
    try {

      RequestInputBO inputBO = new RequestInputBO();
      inputBO.setCode("GNOC_getInfraCableLane");

      //laneCode
      String laneCode = crCableDTO.getLaneCode();
      String startPoint = crCableDTO.getStartPoint();
      String endPoint = crCableDTO.getEndPoint();
      ParameterBO parameterBO = new ParameterBO();
      parameterBO.setName("laneCode");
      parameterBO.setType("STRING");
      if (StringUtils.isStringNullOrEmpty(laneCode)) {
        laneCode = "N/A";
      }
      parameterBO.setValue(laneCode);
      inputBO.getParams().add(parameterBO);
      //startPoint
      parameterBO = new ParameterBO();
      parameterBO.setName("startPoint");
      parameterBO.setType("STRING");
      if (StringUtils.isStringNullOrEmpty(startPoint)) {
        startPoint = "N/A";
      }
      parameterBO.setValue(startPoint);
      inputBO.getParams().add(parameterBO);
      //endPoint
      parameterBO = new ParameterBO();
      parameterBO.setName("endPoint");
      parameterBO.setType("STRING");
      if (StringUtils.isStringNullOrEmpty(endPoint)) {
        endPoint = "N/A";
      }
      parameterBO.setValue(endPoint);
      inputBO.getParams().add(parameterBO);
      if (port == null) {
        createConnect();
      }

      if (port != null) {

        JsonResponseBO dataJson = port.getDataJson(PassTranformer.decrypt(username), PassTranformer.decrypt(password), inputBO);
        Type type = new TypeToken<HashMap<String, List<TempCableDTO>>>() {
        }.getType();
        HashMap<String, List<TempCableDTO>> map = new Gson().fromJson(dataJson.getDataJson(), type);
        if (map != null && map.get("data") != null) {
          res = new ArrayList<>();
          for (TempCableDTO temp : map.get("data")) {
            CrCableDTO cableDTO = new CrCableDTO();
            cableDTO.setCableCode(temp.getLANE_CODE());
            cableDTO.setStartPoint(temp.getSTART_POINT());
            cableDTO.setEndPoint(temp.getEND_POINT());
            cableDTO.setNationCode(crCableDTO.getNationCode());
            cableDTO.setType("1");//truongnt sua doan cap = 0, tuyen cap = 1
            res.add(cableDTO);
          }
        }

      }
      if (res != null && res.size() > 0) {
        int totalSize = res.size();
        int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
        datatable.setTotal(totalSize);
        datatable.setPages(pageSize);
        List<CrCableDTO> crSubList = (List<CrCableDTO>) DataUtil
            .subPageList(res, page, size);
        crSubList.get(0).setPage(page);
        crSubList.get(0).setPageSize(pageSize);
        datatable.setData(crSubList);
      }
      return datatable;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return datatable;
  }

  @Override
  public Datatable getAllCableInLane(CrCableDTO crCableDTO) {
    Datatable datatable = new Datatable();
    int page = crCableDTO.getPage();
    int size = crCableDTO.getPageSize();
    size = (size > 0) ? size : 5;
    crCableDTO.setSortName(null);
    crCableDTO.setSortType(null);
    crCableDTO.setPage(1);
    crCableDTO.setPageSize(Integer.MAX_VALUE);
    List<CrCableDTO> lstData = new ArrayList<>();
    try {

      boolean check = false;
      String[] laneCode = crCableDTO.getLaneCode().split(";");
      String nationCode = crCableDTO.getNationCode();

      if (StringUtils.isStringNullOrEmpty(nationCode)) {
        return datatable;
      }
      if (laneCode != null && laneCode.length > 0) {
        check = true;
      }
      if (check) {
        for (int i = 0; i < laneCode.length; i++) {
          List<CrCableDTO> lstTemp = wsnimsInfraPort.getAllCableInLane(laneCode[i]);
          if (lstTemp != null && !lstTemp.isEmpty()) {
            lstData.addAll(lstTemp);
          }
        }
      }
      if (lstData != null && lstData.size() > 0) {
        int totalSize = lstData.size();
        int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
        datatable.setTotal(totalSize);
        datatable.setPages(pageSize);
        List<CrCableDTO> crSubList = (List<CrCableDTO>) DataUtil
            .subPageList(lstData, page, size);
        crSubList.get(0).setPage(page);
        crSubList.get(0).setPageSize(pageSize);
        datatable.setData(crSubList);
      }
      return datatable;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return datatable;
  }

  @Override
  public List<InfraSleevesBO> getSleevesInCable(String sleeveCode, Long purpose, String cableCode) {
    List<InfraSleevesBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getSleevesInCable(sleeveCode, purpose, cableCode);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  @Override
  public Datatable getLinkInfo(CrCableDTO crCableDTO) {
    Datatable datatable = new Datatable();
    int page = crCableDTO.getPage();
    int size = crCableDTO.getPageSize();
    size = (size > 0) ? size : 5;
    crCableDTO.setSortName(null);
    crCableDTO.setSortType(null);
    crCableDTO.setPage(1);
    crCableDTO.setPageSize(Integer.MAX_VALUE);
    List<LinkInfoDTO> lstLink = new ArrayList<>();
    try {
      if (crCableDTO.getLstCableCode() != null) {
        lstLink = wsnimsInfraPort.getLinkInfo(crCableDTO.getLstCableCode());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (lstLink != null && lstLink.size() > 0) {
      int totalSize = lstLink.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
      List<LinkInfoDTO> crSubList = (List<LinkInfoDTO>) DataUtil
          .subPageList(lstLink, page, size);
      crSubList.get(0).setPage(page);
      crSubList.get(0).setPageSize(pageSize);
      datatable.setData(crSubList);
    }
    return datatable;
  }

  @Override
  public List<CrCableDTO> getListCrCableByCondition(CrInsiteDTO crInsiteDTO) {
    return crCableRepository
        .getListCrCableByCondition(crInsiteDTO);
  }

  @Override
  public List<CrCableDTO> getListCrCableDTO(CrCableDTO crCableDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return crCableRepository
        .getListCrCableDTO(crCableDTO, rowStart, maxRow, sortType, sortFieldList);
  }
}
