/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.viettel.bccs2.CauseFrom;
import com.viettel.bccs2.ProblemFrom;
import com.viettel.bccs2.ProblemGroupDTO;
import com.viettel.bccs2.ProblemTypeDTO;
import com.viettel.bccs2.ResponseListParrentProbGroup;
import com.viettel.bccs2.SpmRespone;
import com.viettel.bccs2.SpmServiceImpl;
import com.viettel.bccs2.SpmServiceImplService;
import com.viettel.bccs2.WsResponseProblemType;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.HeaderHandlerResolver;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.springframework.stereotype.Service;

@Service
public class WSBCCSV2Port {

  SpmServiceImpl port = null;
  SpmServiceImpl portVTP;
  SpmServiceImpl portNAT;
  SpmServiceImpl portCMR;
  SpmServiceImpl portOther;
  SpmServiceImpl portTZN;
  SpmServiceImpl portSTL;
  SpmServiceImpl portMVT;
  SpmServiceImpl portVTL;
  SpmServiceImpl portBRD;
  SpmServiceImpl portVTC;
  SpmServiceImpl portMYT;


  private void createConnect(
      CfgServerNocDTO cfgServerNocDTO) throws MalformedURLException, Exception {
    URL baseUrl = SpmServiceImplService.class.getResource(".");
    URL url = new URL(baseUrl, cfgServerNocDTO.getLink());
    SpmServiceImplService service = new SpmServiceImplService(url,
        new QName(cfgServerNocDTO.getLinkName(), cfgServerNocDTO.getServerName()));
    HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(cfgServerNocDTO.getUserName(),
        cfgServerNocDTO.getPass());
    service.setHandlerResolver(handlerResolver);
    if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portNAT = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portVTP = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portCMR = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portTZN = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portSTL = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portMVT = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portVTL = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portBRD = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portVTC = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      portMYT = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())) {
      port = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    } else {
      portOther = (SpmServiceImpl) setTimeOut(service.getSpmServiceImplPort());
    }

  }

  /**
   * @param webServiceType 1: Cập nhật thông tin. (Ví dụ nguyên nhân quá hạn) 2: Đóng khiếu nại 3:
   * Tạm đóng
   */
  public SpmRespone updateCompProcessing(int webServiceType, ProblemFrom problemFrom,
      CfgServerNocDTO cfgServerNocDTO) throws Exception {
    SpmRespone res = null;
    try {
      prepareConnect(cfgServerNocDTO);

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      } else if (portOther != null) {
        res = portOther.updateProblemProcessing(webServiceType, 3, problemFrom, "", "", "");
      }

    } catch (Exception e) {
      throw e;
    }
    return res;
  }

  public SpmRespone getListSolution(CfgServerNocDTO cfgServerNocDTO) throws Exception {
    SpmRespone res = null;
    try {
      prepareConnect(cfgServerNocDTO);

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListSolution();
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListSolution();
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListSolution();
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListSolution();
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListSolution();
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListSolution();
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListSolution();
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListSolution();
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListSolution();
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListSolution();
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListSolution();
      } else if (portOther != null) {
        res = portOther.getListSolution();
      }
      if (port != null) {
        res = port.getListSolution();
      }
    } catch (Exception e) {
      throw e;
    }
    return res;
  }

  public com.viettel.bccs2.SpmRespone getListUserExcecuteProblems(
      List<Long> lstTroubleId, CfgServerNocDTO cfgServerNocDTO) throws Exception {
    SpmRespone res = null;
    try {
      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT == null) {
        createConnect(cfgServerNocDTO);
      } else if (portOther == null) {
        createConnect(cfgServerNocDTO);
      }

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListUserExcecuteProblems(lstTroubleId);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListUserExcecuteProblems(lstTroubleId);
      } else if (portOther != null) {
        res = portOther.getListUserExcecuteProblems(lstTroubleId);
      }
    } catch (Exception e) {
      throw e;
    }
    return res;
  }

  public static Object setTimeOut(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.connect.timeout", 60000);
    ((BindingProvider) port).getRequestContext()
        .put("com.sun.xml.internal.ws.request.timeout", 60000);
    return port;
  }

  public SpmRespone getListCause(CfgServerNocDTO cfgServerNocDTO, int serviceType,
      CauseFrom causeFilter) throws Exception {

    SpmRespone res = null;
    List<Long> lstGroupId = new ArrayList<Long>();
    lstGroupId.add(0L);
    lstGroupId.add(1L);
    StringUtils.printLogData("---LOG Input nguyen nhan 3 cap ----", causeFilter, CauseFrom.class);
    try {
      prepareConnect(cfgServerNocDTO);
      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListCause(serviceType, causeFilter);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListCause(serviceType, causeFilter);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListCause(serviceType, causeFilter);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListCause(serviceType, causeFilter);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListCause(serviceType, causeFilter);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListCause(serviceType, causeFilter);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListCause(serviceType, causeFilter);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListCause(serviceType, causeFilter);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListCause(serviceType, causeFilter);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListCause(serviceType, causeFilter);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListCause(serviceType, causeFilter);
      } else if (portOther != null) {
        res = portOther.getListCause(serviceType, causeFilter);
      }
    } catch (Exception e) {
      throw e;
    }
    StringUtils.printLogData("---LOG output nguyen nhan 3 cap ----", res, SpmRespone.class);
    return res;
  }

  public SpmRespone getCauseExistError(Long parentId, CfgServerNocDTO cfgServerNocDTO)
      throws Exception {

    SpmRespone res = null;
    List<Long> lstGroupId = new ArrayList<Long>();
    lstGroupId.add(0L);
    lstGroupId.add(1L);
    try {
      prepareConnect(cfgServerNocDTO);

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListCauseErrorExpire(2, lstGroupId, parentId);
      } else if (portOther != null) {
        res = portOther.getListCauseErrorExpire(2, lstGroupId, parentId);
      }
    } catch (Exception e) {
      throw e;
    }
    return res;
  }

  private void prepareConnect(CfgServerNocDTO cfgServerNocDTO)
      throws MalformedURLException, Exception {
    if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portNAT == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTP == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portCMR == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portTZN == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portSTL == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portMVT == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTL == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portBRD == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portVTC == null) {
      createConnect(cfgServerNocDTO);
    } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && portMYT == null) {
      createConnect(cfgServerNocDTO);
    } else if (portOther == null) {
      createConnect(cfgServerNocDTO);
    }
  }

  //Lay danh sach nhom--Nang cap tu ver 1
  public List<ProblemGroupDTO> getListProblemGroupParent(CfgServerNocDTO cfgServerNocDTO)
      throws Exception {
    ResponseListParrentProbGroup res = null;
    try {
      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT == null) {
        createConnect(cfgServerNocDTO);
      } else if (portOther == null) {
        createConnect(cfgServerNocDTO);
      }

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListProblemGroupParent();
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListProblemGroupParent();
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListProblemGroupParent();
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListProblemGroupParent();
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListProblemGroupParent();
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListProblemGroupParent();
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListProblemGroupParent();
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListProblemGroupParent();
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListProblemGroupParent();
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListProblemGroupParent();
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListProblemGroupParent();
      } else if (portOther != null) {
        res = portOther.getListProblemGroupParent();
      }
    } catch (Exception e) {
      throw e;
    }
    return res != null ? res.getListProblemGroup() : null;
  }

  //Lay danh sach the loai -- Nang cap tu ver 1
  public List<ProblemGroupDTO> getListProblemGroupByParrenId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO) throws Exception {
    ResponseListParrentProbGroup res = null;
    try {
      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT == null) {
        createConnect(cfgServerNocDTO);
      } else if (portOther == null) {
        createConnect(cfgServerNocDTO);
      }

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListProblemGroupByParrenId(probGroupId);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListProblemGroupByParrenId(probGroupId);
      } else if (portOther != null) {
        res = portOther.getListProblemGroupByParrenId(probGroupId);
      }
    } catch (Exception e) {
      throw e;
    }
    return res != null ? res.getListProblemGroup() : null;
  }

  //Lay danh sach loai -- Nang cap tu ver 1
  public List<ProblemTypeDTO> getListPobTypeByGroupId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO) throws Exception {
    WsResponseProblemType res = null;
    try {
      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC == null) {
        createConnect(cfgServerNocDTO);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT == null) {
        createConnect(cfgServerNocDTO);
      } else if (portOther == null) {
        createConnect(cfgServerNocDTO);
      }

      if ("BCCS".equalsIgnoreCase(cfgServerNocDTO.getInsertSource()) && port != null) {
        res = port.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_NAT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portNAT != null) {
        res = portNAT.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_VTP".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTP != null) {
        res = portVTP.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_CMR".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portCMR != null) {
        res = portCMR.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_TZN".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portTZN != null) {
        res = portTZN.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_STL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portSTL != null) {
        res = portSTL.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_MVT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMVT != null) {
        res = portMVT.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_VTL".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTL != null) {
        res = portVTL.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_BRD".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portBRD != null) {
        res = portBRD.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_VTC".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portVTC != null) {
        res = portVTC.getListPobTypeByGroupId(probGroupId);
      } else if ("BCCS_MYT".equalsIgnoreCase(cfgServerNocDTO.getInsertSource())
          && portMYT != null) {
        res = portMYT.getListPobTypeByGroupId(probGroupId);
      } else if (portOther != null) {
        res = portOther.getListPobTypeByGroupId(probGroupId);
      }
    } catch (Exception e) {
      throw e;
    }
    return res != null ? res.getListProblemType() : null;
  }

}
