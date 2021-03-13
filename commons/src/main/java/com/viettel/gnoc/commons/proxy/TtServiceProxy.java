package com.viettel.gnoc.commons.proxy;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.commons.dto.TroubleStatisticFormDTO;
import com.viettel.gnoc.commons.dto.WSNocprov4DTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tt-service")
public interface TtServiceProxy {

  @PostMapping("/CfgServerNOC/getListCfgServerNocByCondition")
  public List<CfgServerNocDTO> getListCfgServerNocByCondition(@RequestBody
      WSNocprov4DTO wsNocprov4DTO) throws Exception;

  @PostMapping("/TroublesServiceForCC/onUpdateTroubleFromWo")
  public ResultDTO onUpdateTroubleFromWo(@RequestBody TroublesDTO troublesDTO) throws Exception;

  @PostMapping("/Troubles/countTicketByShift")
  public List<TroublesInSideDTO> countTicketByShift(@RequestBody TroublesInSideDTO troublesDTO);

  @PostMapping("/Troubles/onUpdateTroubleMobile")
  public ResultDTO onUpdateTroubleMobile(@RequestBody TroublesDTO troublesDTO) throws Exception;

  @PostMapping("/Troubles/onClosetroubleFromWo")
  public ResultDTO onClosetroubleFromWo(@RequestBody TroublesDTO troublesDTO);

  @PostMapping("/Troubles/searchParentTTForCR")
  public Datatable searchParentTTForCR(@RequestBody TroublesInSideDTO dto);

  @GetMapping("/Troubles/checkAlarmNOC/troubleCode/{troubleCode}/typeWo/{typeWo}")
  public ResultDTO checkAlarmNOC(@PathVariable("troubleCode") String troubleCode,
      @PathVariable("typeWo") String typeWo);

  @GetMapping("/Troubles/getTroubleByCode/troubleCode/{troubleCode}")
  public List<TroublesDTO> getTroubleByCode(@PathVariable("troubleCode") String troubleCode);

  @PostMapping("/Troubles/getStatisticTroubleTotal")
  public List<TroubleStatisticForm> getStatisticTroubleTotal(
      @RequestBody TroubleStatisticFormDTO troubleStatisticFormDTO);

  @PostMapping("/Troubles/getInfoTicketForAMI")
  public List<TroublesDTO> getInfoTicketForAMI(@RequestBody TroublesDTO troublesDTO);

  // kienpv
  @PostMapping("/cfgMapNetworkLevel/getListCfgMapNetLevelIncTypeDTO")
  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  @PostMapping("/cfgMapNetworkLevel/getListCfgMapNetLevelIncTypeDatatable")
  Datatable getListCfgMapNetLevelIncTypeDatatable(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  @PutMapping("/cfgMapNetworkLevel/updateCfgMapNetLevelIncType")
  ResultInSideDto updateCfgMapNetLevelIncType(
      @RequestBody @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  @DeleteMapping("/cfgMapNetworkLevel/deleteCfgMapNetLevelIncType/id{id}")
  ResultInSideDto deleteCfgMapNetLevelIncType(@PathVariable(value = "id") Long id);

  @GetMapping("/cfgMapNetworkLevel/findCfgMapNetLevelIncTypeById/id{id}")
  CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(@PathVariable(value = "id") Long id);

  @PostMapping("/cfgMapNetworkLevel/insertCfgMapNetLevelIncType")
  ResultInSideDto insertCfgMapNetLevelIncType(
      @RequestBody @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  @GetMapping("/cfgMapNetworkLevel/netWorkRiskList")
  Map<?, ?> netWorkRiskList();

  @GetMapping("/cfgMapNetworkLevel/netWorkLevelList")
  Map<?, ?> netWorkLevelList();
  // kienpv

  @PostMapping("/Troubles/getListProblemGroupParent")
  public List<ProblemGroupDTO> getListProblemGroupParent(@RequestBody
      CfgServerNocDTO cfgServerNocDTO) throws Exception;

  @PostMapping("/Troubles/getListProblemGroupByParrenId/probGroupId{probGroupId}")
  public List<ProblemGroupDTO> getListProblemGroupByParrenId(
      @PathVariable(value = "probGroupId") Long probGroupId, @RequestBody
      CfgServerNocDTO cfgServerNocDTO) throws Exception;

  @PostMapping("/Troubles/getListPobTypeByGroupId/probGroupId{probGroupId}")
  public List<ProblemTypeDTO> getListPobTypeByGroupId(
      @PathVariable(value = "probGroupId") Long probGroupId, @RequestBody
      CfgServerNocDTO cfgServerNocDTO) throws Exception;

  @PostMapping("/Troubles/getListReason")
  List<CatReasonInSideDTOSearch> getListReason(@RequestBody CatReasonInSideDTO reasonDto);
}
