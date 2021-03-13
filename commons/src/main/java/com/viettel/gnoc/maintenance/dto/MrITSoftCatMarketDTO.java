package com.viettel.gnoc.maintenance.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrITSoftCatMarketEntity;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class MrITSoftCatMarketDTO extends BaseDto {

  @NotEmpty(message = "{validation.mrITSoftCatMarketDTO.null.marketCode}")
  private String marketCode;
  @NotEmpty(message = "{validation.mrITSoftCatMarketDTO.null.marketName}")
  private String marketName;
  @NotEmpty(message = "{validation.mrITSoftCatMarketDTO.null.countryCode}")
  private String countryCode;
  private String countryName;

  public MrITSoftCatMarketDTO(String marketCode, String marketName, String countryCode) {
    this.marketCode = marketCode;
    this.marketName = marketName;
    this.countryCode = countryCode;
  }

  public MrITSoftCatMarketDTO(String countryCode) {
    this.countryCode = countryCode;
  }

  public MrITSoftCatMarketEntity toEntity() {
    try {
      MrITSoftCatMarketEntity model = new MrITSoftCatMarketEntity(marketCode, marketName,
          countryCode);
      return model;
    } catch (Exception e) {
      return null;
    }
  }
}
