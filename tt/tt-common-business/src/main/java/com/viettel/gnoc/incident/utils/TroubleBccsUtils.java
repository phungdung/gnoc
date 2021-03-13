
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.bccs2.CauseDTO;
import com.viettel.bccs2.CauseErrorExpireDTO;
import com.viettel.bccs2.ProblemFrom;
import com.viettel.bccs2.SpmRespone;
import com.viettel.bccs2.TroubleNetworkSolutionDTO;
import com.viettel.bccs2.UpdateProblemProcessing;
import com.viettel.bccs2.UserExecuteProblemFrom;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.GnocResponseDTO;
import com.viettel.gnoc.incident.dto.TokenDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.soc.spm.service.ResultDTO;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author quangdx
 */
@Slf4j
@Service
public class TroubleBccsUtils {

  public static final int CONNECT_TIMEOUT = 60000;//ms
  public static final int REQUEST_TIMEOUT = 60000;//ms

  @Autowired
  WSBCCSV2Port wsbccsv2Port;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  WSBCCS2Port wsbccs2Port;

  public ResultDTO updateCompProcessing(MessagesRepository repository, int webServiceType,
      TroublesInSideDTO tForm, CfgServerNocDTO nocDTO) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    try {
      log.info("Step 1: updateCompProcessing ");
      if (nocDTO != null && "BCCS_MYT".equalsIgnoreCase(nocDTO.getInsertSource())) {
        log.info("Step2: call BCCS_MYT");
        return wsbccs2Port.updateCompProcessing(messagesRepository, webServiceType, tForm, nocDTO);
      } else {
        log.info("Step2: call BCCS");
      }
      if (tForm.getComplaintId() != null) {
        ProblemFrom problemFrom = new ProblemFrom();
        problemFrom.setProblemId(tForm.getComplaintId());
        problemFrom.setListProblemId(tForm.getLstComplaint());
        //noi dung xu ly
        String content;
        if (!StringUtils.isStringNullOrEmpty(tForm.getWorkLog())) {
          content = tForm.getWorkLog().length() >= 2000 ? tForm.getWorkLog().substring(0, 1999)
              : tForm.getWorkLog();
        } else {
          content = "System GNOC " + I18n.getLanguage("incident.update");

        }
        problemFrom.setResultContent(content);
        //ma su cố
        problemFrom.setTicketCode(tForm.getTroubleCode());
        //mang
        problemFrom.setArrTroubleCode(tForm.getTypeName());
        //trang thai
        problemFrom.setTicketStatus(tForm.getStateName());
        //nhan vien xu ly
        problemFrom.setUserName(tForm.getProcessingUserName());
        //don vi xu ly
        problemFrom.setDepId(
            StringUtils.isStringNullOrEmpty(tForm.getProcessingUnitId()) == true ? null : Long
                .parseLong(tForm.getProcessingUnitId()));
        problemFrom.setDepProcess(tForm.getProcessingUnitName());
        //NN qua han
        problemFrom.setCauseErrorExpireId(
            StringUtils.isStringNullOrEmpty(tForm.getReasonOverdueId2()) == true ? null : Long
                .parseLong(tForm.getReasonOverdueId2()));
        // NN tu CSKH
        problemFrom.setCauseId(
            StringUtils.isStringNullOrEmpty(tForm.getReasonLv3Id()) == true ? null : Long
                .parseLong(tForm.getReasonLv3Id()));
        //thoi gian hen
        problemFrom.setSuspendToDate(DateUtil.date2ddMMyyyyHHMMss(tForm.getDeferredTime()));
        //Thoi điem hen
        problemFrom.setExtendDate(DateTimeUtils.getSysDateTime());
        //thoi gian hoan thanh
        problemFrom.setTroubleCompletedTime(DateUtil.date2ddMMyyyyHHMMss(tForm.getClearTime()));
        //thoi gian du kien
        problemFrom.setExtendTime(DateUtil.date2ddMMyyyyHHMMss(tForm.getEstimateTime()));
        //nguyen nhan
        String rootCause = "";
        if (tForm.getRootCause() != null) {
          rootCause = I18n.getLanguage("trouble.rootCause") + ": " + tForm.getRootCause();
        }
        problemFrom.setRootCause(tForm.getRootCause());
        //giai phap
        problemFrom.setSolution(
            rootCause + " \n " + I18n.getLanguage("trouble.solution") + ": " + tForm
                .getWorkArround());
        //trang thai
        problemFrom.setWoStatus(tForm.getStateWo());
        //loai tam dong
        problemFrom.setPendingType(tForm.getDeferType() == null ? null
            : DateUtil.date2ddMMyyyyHHMMss(tForm.getEstimateTime()));
        //nguoi tam dong
        problemFrom.setExtendUser(tForm.getProcessingUserName());
        //so lan tam dong
        problemFrom.setNumberExtend(tForm.getNumPending() == null ? 0L : tForm.getNumPending());

        if ("7".equals(tForm.getStateWo())) {
          problemFrom.setExtendStatus(7L);
          webServiceType = 3;
        }
//            if ("MOBILE".equals(tForm.getSpmName())) {//tu wo
//                problemFrom.setTotalCustApointTimeGnoc(tForm.getTotalPendingTimeGnoc());
//                problemFrom.setTroubleTotalProcessTime(tForm.getTotalProcessTimeGnoc());
//                problemFrom.setProgressGnoc(tForm.getEvaluateGnoc() == null ? null : Long.parseLong(tForm.getEvaluateGnoc()));
//            } else {
        //thoi gian xu ly GNOC
        problemFrom.setTotalCustApointTimeGnoc("0");
        problemFrom.setTroubleTotalProcessTime(
            tForm.getTimeUsed() == null ? null : String.valueOf(tForm.getTimeUsed()));
        if (tForm.getRemainTime() != null && Double.parseDouble(tForm.getRemainTime()) < 0) {
          problemFrom.setProgressGnoc(0L);
        } else {
          problemFrom.setProgressGnoc(1L);
        }
//            }

        //ma vung lom
        problemFrom.setTroubleAreaCode(tForm.getConcave());
        String infoTicket = tForm.getInfoTicket();
        if (tForm.getIsStationVipName() != null && tForm.getIsStationVipName().toLowerCase()
            .contains("extend")) {
          infoTicket = tForm.getIsStationVipName() + " \n " + infoTicket;
        }
        problemFrom.setInfoTicket(infoTicket);
        //WO day them loai WO khi dong
        String result = "";
        if (tForm.getCustomerTimeDesireTo() != null && ("1".equals(tForm.getCustomerTimeDesireTo())
            || "5".equals(tForm.getCustomerTimeDesireTo()) || "6"
            .equals(tForm.getCustomerTimeDesireTo()))) {
          result = tForm.getCustomerTimeDesireTo();
        }
        if (!StringUtils.isStringNullOrEmpty(tForm.getCloseCode())) {
          CatItemDTO catItemDTO = catItemRepository.getCatItemById(tForm.getCloseCode());
          if (catItemDTO != null) {
            result = catItemDTO.getItemName();
          }
        }
        problemFrom.setDetailResultGnoc(result);
        //thoi gian phan hoi
        if (webServiceType == 2 || webServiceType == 3 || (webServiceType == 1
            && tForm.getIsStationVipName() != null && tForm.getIsStationVipName().toLowerCase()
            .contains("extend"))) {
          problemFrom.setResponseDateGnoc(DateTimeUtils.getSysDateTime());
        }
        //WSBCCS2Port port = new WSBCCS2Port();
        SpmRespone respone;
        try {
          if (webServiceType == 2 || webServiceType == 3) {
            List<Long> lst = new ArrayList<>();
            lst.add(problemFrom.getProblemId());
            respone = wsbccsv2Port.getListUserExcecuteProblems(lst, nocDTO);
            List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
            if (respone != null && "00".equals(respone.getErrorCode())) {
              List<UserExecuteProblemFrom> lstUser = respone.getLstUserExecuteProblem();
              if (lstUser != null && !lstUser.isEmpty()) {
                for (UserExecuteProblemFrom executeFrom : lstUser) {
                  MessagesDTO messagesDTO = new MessagesDTO();
                  String sms;
                  if (webServiceType == 2) {
                    sms = I18n.getLanguage("incident.complete.send.sms.to.cc");
                  } else {
                    sms = I18n.getLanguage("incident.delay.send.sms.to.cc");
                  }
                  sms = sms.replaceAll("#troubleCode#",
                      tForm.getTroubleName() + " " + tForm.getTroubleCode());

                  messagesDTO.setContent(sms);
                  messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                  messagesDTO.setReceiverId("1");
                  messagesDTO.setReceiverUsername(executeFrom.getUserName());
                  messagesDTO.setReceiverPhone(executeFrom.getTel());
                  messagesDTO.setSmsGatewayId("5");
                  messagesDTO.setStatus("0");
                  if (!StringUtils.isStringNullOrEmpty(executeFrom.getUserName()) && !StringUtils
                      .isStringNullOrEmpty(executeFrom.getTel())) {
                    lsMessagesDTOs.add(messagesDTO);
                  }
                }
                repository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
              }
            }
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
        log.info(
            " -- TicketCode -- " + problemFrom.getTicketCode() + " nocDTO.getLink() " + nocDTO
                .getLink() + " webServiceType " + webServiceType);
        log.info(" -- TicketCode --  {}", problemFrom.getTicketCode());
        log.info(" -- Link WS NOC.GETLINK(): -- {}", nocDTO.getLink());
        log.info(" webServiceType: -- {}", webServiceType);
        log.info(" problemFrom: -- {}", problemFrom);
        respone = wsbccsv2Port.updateCompProcessing(webServiceType, problemFrom, nocDTO);

        if (respone != null && "00".equals(respone.getErrorCode())) {
          resultDTO.setKey(RESULT.SUCCESS);
          resultDTO.setMessage(RESULT.SUCCESS);
        } else if (respone != null) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(respone.getErrorCode() + " " + respone.getErrorDescription());
          throw new Exception(
              I18n.getLanguage("incident.complete.to.cc") + ": " + respone.getErrorDescription());
        } else {
          resultDTO.setKey(RESULT.FAIL);
          throw new Exception(I18n.getLanguage("incident.complete.to.cc"));
        }
      } else {
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage(RESULT.SUCCESS);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
      throw ex;
    }
    return resultDTO;
  }

  public String getToken(CfgServerNocDTO nocDTO) {

    String url = nocDTO.getLink() + "oauth/token?grant_type=client_credentials";
    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    httpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
    httpRequestFactory.setReadTimeout(REQUEST_TIMEOUT);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Basic Z25vY19zeXN0ZW06YmNjczM=");

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
    TokenDTO tokenDTO = restTemplate.postForObject(url, request, TokenDTO.class);
    return tokenDTO.getAccess_token();
  }

  // lay nguyen nhan 3 cap
  public List<CauseDTO> getCompCauseDTO(String parentId, String serviceTypeId, String probGroupId,
      CfgServerNocDTO nocDTO) {
    List<CauseDTO> lst = new ArrayList<>();
    String url = nocDTO.getLink()
        + "problem/complains/causes?serviceTypeId=:serviceTypeId&probGroupId=:probGroupId";

    url = url.replace(":serviceTypeId", serviceTypeId);
    url = url.replace(":probGroupId", probGroupId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Authorization", "Bearer " + getToken(nocDTO));
    httpHeaders.set("Accept-Language", nocDTO.getServerName()); // en ngon ngu, MZ la thi truong

    HttpEntity entity = new HttpEntity(httpHeaders);
    RestTemplate restTemplate = new RestTemplate();

    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.put("causeParentId", Arrays.asList(parentId));

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromUriString(url) // rawValidURl = http://example.com/hotels
        .queryParams(
            (LinkedMultiValueMap<String, String>) map); // The allRequestParams must have been built for all the query params
    UriComponents uriComponents = builder.build().encode();

    ResponseEntity<String> causeResponse = restTemplate
        .exchange(uriComponents.toUri(), HttpMethod.GET, entity, String.class);

    if (HttpStatus.OK == causeResponse.getStatusCode()) { //check 200 OK
      Type listType = new TypeToken<ArrayList<CauseDTO>>() {
      }.getType();
      lst = new Gson().fromJson(causeResponse.getBody(), listType);

    }
    return lst;
  }

  //lay nn qua han
  public List<CauseErrorExpireDTO> getCauseErrorExpire(String parentId, CfgServerNocDTO nocDTO
  ) {
    List<CauseErrorExpireDTO> lst = new ArrayList<>();
    String url = nocDTO.getLink() + "problem/complains/cause-error-expires";

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Authorization", "Bearer " + getToken(nocDTO));
    httpHeaders.set("Accept-Language", nocDTO.getServerName()); // en ngon ngu, MZ la thi truong

    HttpEntity entity = new HttpEntity(httpHeaders);

//        Map<String, String> params = new HashMap<String, String>();
//        params.put("parentId", parentId);
//        params.put("status", "1");
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.put("parentId", Arrays.asList(parentId));
    map.put("status", Arrays.asList("1"));

    UriComponentsBuilder builder = UriComponentsBuilder
        .fromUriString(url) // rawValidURl = http://example.com/hotels
        .queryParams(
            (LinkedMultiValueMap<String, String>) map); // The allRequestParams must have been built for all the query params
    UriComponents uriComponents = builder.build().encode();

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> causeResponse = restTemplate
        .exchange(uriComponents.toUri(), HttpMethod.GET, entity, String.class);
    if (HttpStatus.OK == causeResponse.getStatusCode()) { //check 200 OK
      Type listType = new TypeToken<ArrayList<CauseErrorExpireDTO>>() {
      }.getType();
      StringUtils.printLogData("call BCCS3: ", causeResponse.getBody(), String.class);
      lst = new Gson().fromJson(causeResponse.getBody(), listType);

    }
    return lst;
  }

  // lay nhom gp
  public List<TroubleNetworkSolutionDTO> getGroupSolution(CfgServerNocDTO nocDTO) {
    List<TroubleNetworkSolutionDTO> lst = new ArrayList<>();
    String url = nocDTO.getLink() + "problem/complains/cause-error-expires";

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Authorization", "Bearer " + getToken(nocDTO));
    httpHeaders.set("Accept-Language", nocDTO.getServerName()); // en ngon ngu, MZ la thi truong

    HttpEntity entity = new HttpEntity(httpHeaders);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> causeResponse = restTemplate
        .exchange(url, HttpMethod.GET, entity, String.class);

    if (HttpStatus.OK == causeResponse.getStatusCode()) { //check 200 OK
      Type listType = new TypeToken<ArrayList<TroubleNetworkSolutionDTO>>() {
      }.getType();
      lst = new Gson().fromJson(causeResponse.getBody(), listType);

    }
    return lst;
  }

  //lay d/s nhan vien nhan tin
  public List<UserExecuteProblemFrom> getExecutionUser(String problemIds, CfgServerNocDTO nocDTO) {
    List<UserExecuteProblemFrom> lst = new ArrayList<>();

    String urlCause = nocDTO.getLink() + "problem/execution-users";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Authorization", "Bearer " + getToken(nocDTO));
    httpHeaders.set("Accept-Language", nocDTO.getServerName()); // en ngon ngu, MZ la thi truong

    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.put("problemIds", Arrays.asList(problemIds));

    HttpEntity entity = new HttpEntity(httpHeaders);

    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlCause).queryParams(
        (LinkedMultiValueMap<String, String>) map); // The allRequestParams must have been built for all the query params
    UriComponents uriComponents = builder.build().encode();

    RestTemplate restTemplate1 = new RestTemplate();
    ResponseEntity<String> causeResponse = restTemplate1
        .exchange(uriComponents.toUri(), HttpMethod.GET, entity, String.class);

    if (HttpStatus.OK == causeResponse.getStatusCode()) { //check 200 OK
      Gson gson = new Gson();
      log.info(gson.toJson(causeResponse.getBody()));
      Type listType = new TypeToken<ArrayList<UserExecuteProblemFrom>>() {
      }.getType();
      lst = new Gson().fromJson(causeResponse.getBody(), listType);
    }

    return lst;
  }

  /*
    *webServiceType
    1,Cập nhật thông tin
    2, Hoàn thành
    3, tích hẹn tạm đóng
    */
  public ResultDTO updateComp(int webServiceType, TroublesEntity trouble, TroublesDTO tForm,
      CfgServerNocDTO nocDTO) throws Exception {
    String url = nocDTO.getLink() + "problem/" + trouble.getComplaintId() + "/update";
    ResultDTO resultDTO = new ResultDTO();
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.set("Authorization", "Bearer " + getToken(nocDTO));
      httpHeaders.set("Accept-Language", nocDTO.getServerName()); // en ngon ngu, MZ la thi truong

      httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

      UpdateProblemProcessing updateProblemProcessDTO = new UpdateProblemProcessing();
      ProblemFrom problemFrom = new ProblemFrom();

      if (trouble.getComplaintId() != null) {

        problemFrom.setProblemId(trouble.getComplaintId());
        problemFrom.setListProblemId(trouble.getLstComplaint());
        //noi dung xu ly
        String content;
        if (!StringUtils.isStringNullOrEmpty(trouble.getWorkLog())) {
          content = tForm.getWorkLog().length() >= 2000 ? tForm.getWorkLog().substring(0, 1999)
              : tForm.getWorkLog();
        } else {
          content = "System GNOC " + I18n.getLanguage("common.btn.update");

        }
        problemFrom.setResultContent(content);
        //ma su cố
        problemFrom.setTicketCode(trouble.getTroubleCode());
        //mang
        problemFrom.setArrTroubleCode(tForm.getTypeName());
        //trang thai
        problemFrom.setTicketStatus(tForm.getStateName());
        //nhan vien xu ly
        problemFrom.setUserName(tForm.getProcessingUserName());
        //don vi xu ly
        problemFrom.setDepId(DateTimeUtils.isNullOrEmpty(tForm.getProcessingUnitId()) == true ? null
            : Long.parseLong(tForm.getProcessingUnitId()));
        problemFrom.setDepProcess(tForm.getProcessingUnitName());
        //NN qua han
        problemFrom.setCauseErrorExpireId(
            DateTimeUtils.isNullOrEmpty(tForm.getReasonOverdueId2()) == true ? null
                : Long.parseLong(tForm.getReasonOverdueId2()));
        // NN tu CSKH
        problemFrom.setCauseId(DateTimeUtils.isNullOrEmpty(trouble.getReasonLv3Id()) == true ? null
            : Long.parseLong(trouble.getReasonLv3Id()));
        //thoi gian hen
        problemFrom.setSuspendToDate(tForm.getDeferredTime());
        //Thoi điem hen
        problemFrom.setExtendDate(DateTimeUtils.getSysDateTime());
        //thoi gian hoan thanh
        problemFrom.setTroubleCompletedTime(tForm.getClearTime());
        //thoi gian du kien
        problemFrom.setExtendTime(tForm.getEstimateTime());
        //giai phap
        String rootCause = "";
        if (tForm.getRootCause() != null) {
          rootCause = I18n.getLanguage("trouble.rootCause") + ": " + tForm.getRootCause();
        }
        problemFrom.setSolution(
            rootCause + " \n " + I18n.getLanguage("trouble.solution") + ": " + tForm
                .getWorkArround());
        //trang thai
        problemFrom.setWoStatus(tForm.getStateWo());
        //loai tam dong
        problemFrom.setPendingType(tForm.getDeferType());
        //nguoi tam dong
        problemFrom.setExtendUser(tForm.getProcessingUserName());
        //so lan tam dong
        problemFrom.setNumberExtend(
            tForm.getNumPending() == null ? 0L : Long.parseLong(tForm.getNumPending()));

        if ("7".equals(tForm.getStateWo())) {
          problemFrom.setExtendStatus(7L);
          webServiceType = 3;
        }

        if ("MOBILE".equals(tForm.getSpmName())) {//tu wo
          problemFrom.setTotalCustApointTimeGnoc(tForm.getTotalPendingTimeGnoc());
          problemFrom.setTroubleTotalProcessTime(tForm.getTotalProcessTimeGnoc());
          problemFrom.setProgressGnoc(
              tForm.getEvaluateGnoc() == null ? null : Long.parseLong(tForm.getEvaluateGnoc()));
        } else {
          problemFrom.setTotalCustApointTimeGnoc("0");
          problemFrom.setTroubleTotalProcessTime(tForm.getTimeUsed());
          if (tForm.getRemainTime() != null && Double.parseDouble(tForm.getRemainTime()) < 0) {
            problemFrom.setProgressGnoc(0L);
          } else {
            problemFrom.setProgressGnoc(1L);
          }

        }
        //ma vung lom
        problemFrom.setTroubleAreaCode(tForm.getConcave());

      }
      updateProblemProcessDTO.setProblem(problemFrom);
      updateProblemProcessDTO.setWebServiceType(webServiceType);
      updateProblemProcessDTO.setSource(1);
      Gson gson = new Gson();
      String updateProblemProcessStr = gson.toJson(updateProblemProcessDTO);
      log.info(" updateProblemProcessStr " + updateProblemProcessStr);
      updateProblemProcessStr = URLEncoder.encode(updateProblemProcessStr, "UTF-8");

      MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

      if (tForm.getArrFileName() != null && !tForm.getArrFileName().isEmpty()) {
        int i = 0;
        for (String fileName : tForm.getArrFileName()) {
          ContentDisposition contentDisposition = ContentDisposition
              .builder("form-data")
              .name("files") // cai nay la hang so, de no map voi api
              .filename(fileName)
              .build();
          fileMap.add("Content-Disposition", contentDisposition.toString());
          HttpEntity<byte[]> fileEntity = new HttpEntity<>(tForm.getFileDocumentByteArray().get(i),
              fileMap);

          body.add("fileContent" + ++i, fileEntity);
        }
      }
      body.add("updateProblemProcessStr",
          updateProblemProcessStr); // cai nay la hang so de no map voi api

      //set value to UpdateProblemProcessDTO
      HttpEntity<MultiValueMap<String, Object>> entityReq = new HttpEntity<MultiValueMap<String, Object>>(
          body, httpHeaders);

      try {
        if (webServiceType == 2 || webServiceType == 3) {

          List<UserExecuteProblemFrom> lstUser = getExecutionUser(tForm.getComplaintId(), nocDTO);
          List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();

          if (lstUser != null && !lstUser.isEmpty()) {
            for (UserExecuteProblemFrom executeFrom : lstUser) {
              MessagesDTO messagesDTO = new MessagesDTO();
              String sms;
              if (webServiceType == 2) {
                sms = I18n.getLanguage("incident.complete.send.sms.to.cc");
              } else {
                sms = I18n.getLanguage("incident.delay.send.sms.to.cc");
              }
              sms = sms.replaceAll("#troubleCode#",
                  tForm.getTroubleName() + " " + tForm.getTroubleCode());

              messagesDTO.setContent(sms);
              messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
              messagesDTO.setReceiverId("1");
              messagesDTO.setReceiverUsername(executeFrom.getUserName());
              messagesDTO.setReceiverPhone(executeFrom.getTel());
              messagesDTO.setSmsGatewayId("5");
              messagesDTO.setStatus("0");
              if (!StringUtils.isStringNullOrEmpty(executeFrom.getUserName()) && !StringUtils
                  .isStringNullOrEmpty(executeFrom.getTel())) {
                lsMessagesDTOs.add(messagesDTO);
              }
            }
            messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
          }

        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }

      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters()
          .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
      try {
        ResponseEntity<String> response = restTemplate
            .exchange(url, HttpMethod.POST, entityReq, String.class);
        if (HttpStatus.OK == response.getStatusCode()) { //check 200 OK

          Type listType = new TypeToken<GnocResponseDTO>() {
          }.getType();
          GnocResponseDTO responseDTO = new Gson().fromJson(response.getBody(), listType);
          if (responseDTO != null && responseDTO.isSuccess()) {
            resultDTO.setKey(RESULT.SUCCESS);
            resultDTO.setMessage(RESULT.SUCCESS);
          } else if (responseDTO != null) {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(responseDTO.getMessage());
            throw new Exception(
                I18n.getLanguage("incident.complete.to.cc") + ": " + responseDTO
                    .getMessage());
          } else {
            resultDTO.setKey(RESULT.FAIL);
            throw new Exception(
                I18n.getLanguage("incident.complete.to.cc") + ": " + "1");
          }

        } else {

          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("");
          throw new Exception(
              I18n.getLanguage("incident.complete.to.cc") + ": " + "2");

        }
      } catch (RestClientException e) {
        log.error(e.getMessage(), e);
        resultDTO.setKey(RESULT.FAIL);
        throw new Exception(I18n.getLanguage("incident.complete.to.cc") + ": " + "3");
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      throw new Exception(I18n.getLanguage("incident.complete.to.cc") + ": " + "4");
    }
    return resultDTO;

  }
}
