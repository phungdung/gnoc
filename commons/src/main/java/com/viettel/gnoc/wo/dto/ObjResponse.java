package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.UsersDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ObjResponse {

  private List<FieldForm> lstField;
  List<WoDTOSearch> listChildWo;
  List<UsersDTO> listUserInCd;
  List<WoHistoryDTO> listHistory;
  List<FileNameDTO> listFile;

}
