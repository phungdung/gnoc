package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.sr.model.SRCatalogEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.srCatalog.null.unique}", clazz = SRCatalogEntity.class, uniqueFields = "serviceCode", idField = "serviceId")
public class SRCatalogDTO extends BaseDto implements Cloneable {

  private Long serviceId;
  @NotEmpty(message = "{validation.srCatalog.null.country}")
  private String country;
  private String countryName;
  @NotEmpty(message = "{validation.srCatalog.null.serviceArray}")
  private String serviceArray;
  @NotEmpty(message = "{validation.srCatalog.null.serviceGroup}")
  private String serviceGroup;
  @Size(max = 50, message = "{validation.srCatalog.serviceCode.tooLong}")
  @NotEmpty(message = "{validation.srCatalog.null.serviceCode}")
  private String serviceCode;
  @NotEmpty(message = "{validation.srCatalog.null.serviceName}")
  private String serviceName;
  private String serviceDescription;
  //  @NotEmpty(message = "{validation.srCatalog.null.executionUnit}")
  private String executionUnit;
  @NotEmpty(message = "{validation.srCatalog.null.replyTime}")
  private String replyTime;
  @NotEmpty(message = "{validation.srCatalog.null.executionTime}")
  private String executionTime;
  private String attachFile;
  private String cr;
  private String wo;
  @NotEmpty(message = "{validation.srCatalog.null.status}")
  private String status;
  private String statusName;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;
  private String key;
  private String message;
  private Long isInputChecking;
  private Long isOutputChecking;

  private String serviceIdChild;
  private String serviceCodeChild;
  private String serviceNameChild;
  private String serviceIdSR;
  private String crDesc;
  private String serviceArrayName;
  private String serviceGroupName;
  private String executionUnitDesc;
  private String woDesc;
  private String srChild;
  private String selected;
  private String isNotUsing;

  private Long approve;
  private String replyTimeName;
  private String executionTimeName;

  private String roleCode;
  @NotNull(message = "{validation.srCatalog.null.roleCode}")
  private Long flowExecute;
  private String roleName;
  private String flowExecuteName;
  private String autoCreateSR;

  private List<SRRoleDTO> lstRole;//danh sach nhom xu ly
  private List<UnitDTO> lstUnit;//danh sach don vi xu ly
  private Long isAddDay;
  private String isAddDayName;
  private Long renewDay;
  private String renewDayName;
  private Long autoGenerationCycles;
  private String autoGenerationCyclesName;
  private String createdTimeCRWO;
  private String createdTimeCRWOName;

  //Dung add
  private Long isCrNodes;
  private String monthCycle;
  private Date lastDate;
  private String monthCycleStr;
  private String lastDateStr;
  private Date startAutoDate;
  private String startAutoDateStr;
  private String commentAuto;
  //end

  private String isCrNodesStr;
  private String requireCreateSR;
  private String defaultSortField = "serviceId";
  private List<Long> listChildDelete;
  private List<Long> listRoleUserDelete;
  private List<GnocFileDto> gnocFileDtos;
  private List<SRCatalogChildDTO> srCatalogChildDTOList;
  private List<SRRoleUserInSideDTO> srRoleUserDTOList;
  private String srToOutsideService;
  private String srToOutsideServiceStr;
  private String outsideServiceToSr;
  private String outsideServiceToSrStr;
  private SRConfigDTO srConfig;
  private SRConfigDTO outSideSrConfig;
  private String resultImport;
  private String serviceIdStr;
  private String isUpdateAllUnit;
  //27102020 dungpv add
  private String notification;
  private List<String> lstStatusConfig;
  private String timeClosedSR;
 //20201230 hungtv add
  private List<String> gnocFileDtosName;
  private List<String> srCatalogChildDTOListName;
  private List<String> srRoleUserDTOListName;

  public SRCatalogDTO(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public SRCatalogDTO(Long serviceId, String country, String serviceArray, String serviceGroup,
      String serviceCode, String serviceName, String serviceDescription, String executionUnit,
      String replyTime, String executionTime, String attachFile, String cr, String wo,
      String status, String createdUser, Date createdTime, String updatedUser,
      Date updatedTime, Long approve, Long isInputChecking, Long isOutputChecking, String roleCode,
      Long flowExecute, String autoCreateSR, String createdTimeCRWO, Long isAddDay,
      Long renewDay, Long autoGenerationCycles, Long isCrNodes, String monthCycle, Date lastDate,
      Date startAutoDate, String commentAuto, String notification, String timeClosedSR
  ) {
    this.serviceId = serviceId;
    this.country = country;
    this.serviceArray = serviceArray;
    this.serviceGroup = serviceGroup;
    this.serviceCode = serviceCode;
    this.serviceName = serviceName;
    this.serviceDescription = serviceDescription;
    this.executionUnit = executionUnit;
    this.replyTime = replyTime;
    this.executionTime = executionTime;
    this.attachFile = attachFile;
    this.cr = cr;
    this.wo = wo;
    this.status = status;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.approve = approve;
    this.isInputChecking = isInputChecking;
    this.isOutputChecking = isOutputChecking;
    this.roleCode = roleCode;
    this.flowExecute = flowExecute;
    this.autoCreateSR = autoCreateSR;
    this.createdTimeCRWO = createdTimeCRWO;
    this.isAddDay = isAddDay;
    this.renewDay = renewDay;
    this.autoGenerationCycles = autoGenerationCycles;
    this.isCrNodes = isCrNodes;
    this.monthCycle = monthCycle;
    this.lastDate = lastDate;
    this.startAutoDate = startAutoDate;
    this.commentAuto = commentAuto;
    this.notification = notification;
    this.timeClosedSR = timeClosedSR;
  }

  public SRCatalogDTO clone() throws CloneNotSupportedException {
    return (SRCatalogDTO) super.clone();
  }

  public SRCatalogEntity toEntity() {
    SRCatalogEntity entity = new SRCatalogEntity(serviceId, country, serviceArray, serviceGroup,
        serviceCode, serviceName, serviceDescription, executionUnit, replyTime, executionTime,
        attachFile, cr, wo, status, createdUser, createdTime, updatedUser, updatedTime, approve,
        isInputChecking, isOutputChecking, roleCode, flowExecute, autoCreateSR,
        StringUtils.isNotNullOrEmpty(createdTimeCRWO) ? Double.valueOf(createdTimeCRWO) : null,
        isAddDay, renewDay, autoGenerationCycles, isCrNodes,
        StringUtils.isNotNullOrEmpty(monthCycle) ? Double.valueOf(monthCycle) : null, lastDate,
        startAutoDate,
        commentAuto, notification,timeClosedSR
    );
    return entity;
  }

  public SRCatalogDTO(String key, String message) {
    this.key = key;
    this.message = message;
  }
}
