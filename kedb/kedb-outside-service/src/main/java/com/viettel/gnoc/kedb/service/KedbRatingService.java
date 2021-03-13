package com.viettel.gnoc.kedb.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.kedb.dto.KedbRatingDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author ITSOL
 */

@WebService(serviceName = "KedbRatingService")
public interface KedbRatingService {

  @WebMethod(operationName = "getKedbRating")
  KedbRatingDTO getKedbRating(@WebParam(name = "kedbRatingDTO") KedbRatingDTO kedbRatingDTO);


  @WebMethod(operationName = "insertOrUpdateKedbRating")
  public KedbRatingDTO insertOrUpdateKedbRating(
      @WebParam(name = "kedbRatingDTO") KedbRatingDTO kedbRatingDTO);


  @WebMethod(operationName = "getListKedbRatingDTO")
  public List<KedbRatingDTO> getListKedbRatingDTO(
      @WebParam(name = "kedbRatingDTO") KedbRatingDTO kedbRatingDTO,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  //
  @WebMethod(operationName = "updateKedbRating")
  public String updateKedbRating(@WebParam(name = "kedbRatingDTO") KedbRatingDTO kedbRatingDTO);


  @WebMethod(operationName = "deleteKedbRating")
  public String deleteKedbRating(@WebParam(name = "kedbRatingDTOId") Long id);

  @WebMethod(operationName = "deleteListKedbRating")
  public String deleteListKedbRating(
      @WebParam(name = "kedbRatingListDTO") List<KedbRatingDTO> kedbRatingListDTO);

  @WebMethod(operationName = "findKedbRatingById")
  public KedbRatingDTO findKedbRatingById(@WebParam(name = "kedbRatingDTOId") Long id);

  @WebMethod(operationName = "insertKedbRating")
  public ResultDTO insertKedbRating(@WebParam(name = "kedbRatingDTO") KedbRatingDTO kedbRatingDTO);

  @WebMethod(operationName = "insertOrUpdateListKedbRating")
  public String insertOrUpdateListKedbRating(
      @WebParam(name = "kedbRatingDTO") List<KedbRatingDTO> kedbRatingDTO);

  @WebMethod(operationName = "getSequenseKedbRating")
  public List<String> getSequenseKedbRating(@WebParam(name = "sequenseName") String seqName,
      @WebParam(name = "Size") int... size);

  @WebMethod(operationName = "getListKedbRatingByCondition")
  public List<KedbRatingDTO> getListKedbRatingByCondition(
      @WebParam(name = "lstCondition") List<ConditionBean> lstCondition,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);
}
