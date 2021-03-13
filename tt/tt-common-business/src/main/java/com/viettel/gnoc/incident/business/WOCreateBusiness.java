/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;

/**
 * @author thanhlv12
 */
public interface WOCreateBusiness {

  ResultDTO createWO(TroublesInSideDTO tForm, CfgTimeTroubleProcessDTO config,
      TroublesRepository dao) throws Exception;

  ResultDTO createWOMobile(TroublesInSideDTO tForm, CfgTimeTroubleProcessDTO config,
      TroublesRepository dao) throws Exception;

  ResultDTO createWOForCC(TroublesInSideDTO tForm, TroublesRepository dao) throws Exception;

  ResultDTO changeStatusWo(WoUpdateStatusForm updateForm);
}
