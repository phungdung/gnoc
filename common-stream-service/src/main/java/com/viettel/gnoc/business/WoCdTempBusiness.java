package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface WoCdTempBusiness {

  Datatable getListWoCdTempDTO(WoCdTempInsideDTO woCdTempInsideDTO);

  List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  List<UsersInsideDto> getListUserByCdGroupCBB(Long woGroupId);

  ResultInSideDto insert(WoCdTempInsideDTO woCdTempInsideDTO);

  ResultInSideDto update(WoCdTempInsideDTO woCdTempInsideDTO);

  WoCdTempInsideDTO getDetail(Long woCdTempId);

  ResultInSideDto delete(WoCdTempInsideDTO woCdTempInsideDTO);

  File exportData(WoCdTempInsideDTO woCdTempInsideDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;
}
