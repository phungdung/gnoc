package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SignVofficeDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusForm;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author NamTN
 */
public interface OdBusiness {

  Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO);

  OdDTO findOdById(Long odId);

  OdDTO getDetailOdDTOById(Long odTypeId);

  OdDTO getDetailOdDTOByIdForWS(Long odId);

  ResultInSideDto add(OdDTO odDTO);

  ResultInSideDto edit(OdDTO odDTO);

  ResultInSideDto delete(Long odTypeId);

  ResultInSideDto deleteListOd(List<OdDTO> odDTOList);

  List<String> getSeqOd(int size);

  String insertOrUpdateListOd(List<OdDTO> odDTO);

  File exportData(OdSearchInsideDTO odSearchInsideDTO) throws Exception;

  ResultInSideDto insertOdFromWeb(OdSearchInsideDTO odDTO, List<MultipartFile> files)
      throws Exception;

  ResultInSideDto updateOdFromWeb(OdSearchInsideDTO odDTO, List<MultipartFile> files,
      String userName) throws Exception;

  Datatable getListStatusNext(Long odId, String userName);

  ResultInSideDto signToVoffice(SignVofficeDTO signVofficeDTO, List<MultipartFile> files)
      throws Exception;

  ConfigPropertyDTO getConfigPropertyOd(String key);

  ResultDTO changeStatusOd(OdChangeStatusForm form) throws Exception;

  ResultInSideDto insertLstRelation(OdDTO odDTO);

  List<OdRelationDTO> getRelationsByOdId(Long odId);

  Datatable getListRDMRelationToUpdate(String projectCode, String createDateFrom,
      String createDateTo, int page, int size, Double offSet);

  ResultInSideDto updateOdOtherSystem(OdDTO odDTO);

  ResultDTO insertOdFromOtherSystem(OdDTOSearch odDTO);

  OdTypeDTO getInforByODType(String odTypeCode);
}
