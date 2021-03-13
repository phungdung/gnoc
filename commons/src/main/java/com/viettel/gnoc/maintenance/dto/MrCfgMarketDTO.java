package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrCfgMarketEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrCfgMarketDTO extends BaseDto {

  //Fields
  private Long idMarket;
  private Long marketCode;
  private Long createdUserSoft;
  private String updatedUser;
  private Date updatedTime;
  private String marketName;
  private Long createdUserHard;
  private Long createdUserItSoft;
  private Long createdUserItHard;

  private List<MrCfgMarketDTO> mrCfgMarketDTOList;
  private String createdUserHardName;
  private String createdUserSoftName;
  //Constructor


  public MrCfgMarketDTO(Long idMarket, Long marketCode, Long createdUserSoft,
      String updatedUser, Date updatedTime, String marketName, Long createdUserHard) {
    this.idMarket = idMarket;
    this.marketCode = marketCode;
    this.createdUserSoft = createdUserSoft;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.marketName = marketName;
    this.createdUserHard = createdUserHard;
  }

  public MrCfgMarketDTO(Long idMarket, Long marketCode, Long createdUserSoft,
      String updatedUser, Date updatedTime, String marketName, Long createdUserHard,
      Long createdUserItSoft, Long createdUserItHard) {
    this.idMarket = idMarket;
    this.marketCode = marketCode;
    this.createdUserSoft = createdUserSoft;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.marketName = marketName;
    this.createdUserHard = createdUserHard;
    this.createdUserItSoft = createdUserItSoft;
    this.createdUserItHard = createdUserItHard;
  }

  public MrCfgMarketEntity toEntity() {
    MrCfgMarketEntity model = new MrCfgMarketEntity(
        idMarket,
        marketCode,
        createdUserSoft,
        updatedUser,
        updatedTime,
        marketName,
        createdUserHard,
        createdUserItSoft,
        createdUserItHard
    );
    return model;
  }
}
