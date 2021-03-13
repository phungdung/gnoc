package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.sr.model.SREntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SrInsiteDTO extends BaseDto {

  private Long srId;
  private String srCode;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.country}")
  private String country;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.title}")
  private String title;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.description}")
  @Size(max = 1000, message = "{validation.SrInsiteDTO.description.tooLong}")
  private String description;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.serviceArray}")
  private String serviceArray;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.serviceGroup}")
  private String serviceGroup;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.serviceId}")
  private String serviceId;
  @NotNull(message = "{validation.SrInsiteDTO.null.startTime}")
  private Date startTime;
  private Date endTime;
  @NotEmpty(message = "{validation.SrInsiteDTO.null.status}")
  private String status;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;
  @NotNull(message = "{validation.SrInsiteDTO.null.srUnit}")
  private Long srUnit;
  private String srUser;//người xử lý
  private Date sendDate;
  private Long reviewId;

  //namtn add tab split SR
  private String parentCode;
  private String roleCode;
  private String insertSource;

  private String totalSRProcessTime;
  private String checkingUnit;//đơn vị gửi
  private Date createFromDate;
  private Date createToDate;
  private String serviceName;
  private String unitName;
  private String userId;
  private String statusName;
  private String createdUnit;
  private String isOpenConnect;

  private String serviceArrayName;
  private String serviceGroupName;

  //namtn edit on may 2018
  private String executionTime;
  private String crNumber;
  private String remainExecutionTime;
  private String evaluate;
  private String subOrderId;
  private String serviceCode;
  private String loginUser;
  private String key;
  private String message;

  private String isCloseAble;
  private String isUpdateAble;

  private String evaluateReplyTime;
  private String slaReceiveTime;
  private String pathSrUnit;
  private String replyTime;
  private String actualExecutionTime;

  //namtn edit for process
  private String isSendMessage;
  private String smsContent;
  private String woCode;
  private String mobile;
  private String smsGateWayId;

  private Boolean searchParentUnit;
  private List<SRParamDTO> lstParam;

  private int offset;

  private String state;
  private String actionCode;
  private String returnCode;

  private String username;

  private String flowExecute;
  private String flowExecuteName;
  private String evaluateReason;
  private String executionUnit;

  private String statusRenew;
  private String dayRenew;
  private Long crWoCreatTime;
  private String creatCRWO;
  private String fileContent;
  private List<GnocFileDto> gnocFileDtos;
  private List<GnocFileDto> gnocFileDtosAdd;
  private String wlText;
  private String workLog;
  //  dungpv add
  private Boolean advanced;
  private String countryName;
  private String evaluateReplyTimeDisplay;
  private String remainExecutionTimeCheckStatus;
  private List<SRHisDTO> srHisDTOList;
  private List<SRWorkLogDTO> srWorkLogDTOList;
  private List<SRConfigDTO> lstStatus;
  List<SRMopDTO> lstMopTmp;
  List<CrInsiteDTO> lstCr;
  private Date closedTime;
  private Date newTime;
  private boolean openConnect;
  private boolean serviceNims;
  private boolean serviceAom;
  private boolean autoCreatCR;
  private boolean dvTrungKe;
  private String srChildCode;
  private Long btnSplitSR;
  private Long btnRenew;
  private Long btnApproveRenew;
  private Long btnApproveLevel1;
  private Long btnUnApproveLevel1;
  private Long btnApproveLevel2;
  private Long btnUnApproveLevel2;
  private String RoleAction;
  private String isLeader;
  private SRApproveDTO approveDTO;
  private SRCatalogDTO srCatalogDTO;
  private Long checkUser;

  private boolean addNewFile;
  private boolean addNewWorklog;
  private boolean addNewCr;
  private boolean addNewWo;
  private boolean addNewOd;
  private boolean isExecutionWithCr;
  private boolean isConcludedWithCr;
  private SrInsiteDTO dataCheck;
  private String otherSystemCode;
  private boolean checkStatusClosedNOK;
  private Long flowExecuteId;
  private Long countNok;
  private String createCrSlow;
  private Long isForceClosed;
  private boolean btnClosedSR;
  private Long childAutoId;
  private String reviewViews;
  private String createdUserMobile;
  private String reviewViewsClosed;
  private String reason;
  private String note;
  private Date timeSave;

  public SrInsiteDTO(Long srId, String srCode, String country, String title,
      String description, String serviceArray, String serviceGroup, String serviceId,
      Date startTime, Date endTime, String status, String createdUser, Date createdTime,
      String updatedUser, Date updatedTime, Long srUnit, String srUser, Date sendDate,
      Long reviewId, String parentCode, String roleCode, String insertSource,
      String otherSystemCode, Long countNok, Long isForceClosed, String crNumber, String note,
      Date timeSave, boolean openConnect) {
    this.srId = srId;
    this.srCode = srCode;
    this.country = country;
    this.title = title;
    this.description = description;
    this.serviceArray = serviceArray;
    this.serviceGroup = serviceGroup;
    this.serviceId = serviceId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.srUnit = srUnit;
    this.srUser = srUser;
    this.sendDate = sendDate;
    this.reviewId = reviewId;
    this.parentCode = parentCode;
    this.roleCode = roleCode;
    this.insertSource = insertSource;
    this.otherSystemCode = otherSystemCode;
    this.countNok = countNok;
    this.isForceClosed = isForceClosed;
    this.crNumber = crNumber;
    this.note = note;
    this.timeSave = timeSave;
    this.openConnect = openConnect;
  }

  public SrInsiteDTO(Long srId) {
    this.srId = srId;
  }

  public SREntity toEntity() {
    return new SREntity(srId, srCode, country, title, description, serviceArray, serviceGroup,
        serviceId, startTime, endTime, status, createdUser, createdTime, updatedUser, updatedTime,
        srUnit, srUser, sendDate, reviewId, parentCode, roleCode, insertSource, otherSystemCode,
        countNok, isForceClosed, crNumber, note, timeSave);
  }

  public SRDTO toOutsideDTO() {
    return new SRDTO(StringUtils.isStringNullOrEmpty(srId) ? null : srId.toString(), srCode,
        country, title, description,
        serviceArray,
        serviceGroup,
        serviceId, startTime != null ? DateTimeUtils.date2ddMMyyyyHHMMss(startTime) : null,
        endTime != null ? DateTimeUtils.date2ddMMyyyyHHMMss(endTime) : null
        , status, createdUser,
        createdTime != null ? DateTimeUtils.date2ddMMyyyyHHMMss(createdTime) : null, updatedUser
        , updatedTime != null ? DateTimeUtils.date2ddMMyyyyHHMMss(updatedTime) : null,
        StringUtils.isStringNullOrEmpty(srUnit) ? null : srUnit.toString(), srUser,
        sendDate == null ? null : DateTimeUtils.date2ddMMyyyyHHMMss(sendDate),
        reviewId == null ? null : reviewId.toString()
        , roleCode, parentCode, insertSource, otherSystemCode, note
    );
  }
}
