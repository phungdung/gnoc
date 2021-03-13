package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.ws.dto.CatItemDTO;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemDataTT {

  private String itemId;
  private String itemCode;
  private String itemValue;
  private String itemName;
  private String level;
  private String description;
  private String status;
  private String parentItemId;
  private String parentItemCode;
  private String parentItemName;
  private String keyCode;
  private String position;
  private String isleaf;
  List<ItemDataTT> lstDataCombo;
}
