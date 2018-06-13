package com.kariqu.zwsrv.app.controller;

import com.kariqu.zwsrv.app.model.UserAddressEditInfo;
import com.kariqu.zwsrv.thelib.error.ErrorCode;
import com.kariqu.zwsrv.thelib.exception.ServerException;
import com.kariqu.zwsrv.thelib.http.HttpRequestContext;
import com.kariqu.zwsrv.thelib.model.ErrorResponse;
import com.kariqu.zwsrv.thelib.model.ResponseData;
import com.kariqu.zwsrv.thelib.persistance.entity.UserAddress;
import com.kariqu.zwsrv.thelib.persistance.service.UserAddressService;
import com.kariqu.zwsrv.thelib.security.SecurityUtil;
import com.kariqu.zwsrv.thelib.util.JsonUtil;
import com.kariqu.zwsrv.thelib.util.NumberValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by simon on 26/11/17.
 */

@RestController
@RequestMapping("address/v1")
public class AddressController extends BaseController {

    protected final transient Logger logger = LoggerFactory.getLogger("address");

    @Autowired
    UserAddressService userAddressService;

    @RequestMapping(value="/set_default_address")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData set_default_address(HttpServletRequest request,
                                            @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("address_id");
            int addressId=requestContext.getInegerValue("address_id");
            List<UserAddress> list = userAddressService.findAddressByUserId(currentUserId);
            if (list!=null) {
                for (UserAddress userAddress : list) {
                    if (userAddress.getAddressId()==addressId) {
                        userAddress.setIsDefault(1);
                    } else {
                        userAddress.setIsDefault(0);
                    }
                }
                userAddressService.save(list);
                return new ResponseData();
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }


    @RequestMapping(value="/add_address")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData add_addresses(HttpServletRequest request,
                                       @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("edit_info");
            String jsonString=requestContext.getStringValue("edit_info");
            logger.info("add_address:{} ",jsonString!=null?jsonString:"");
            if (jsonString!=null) {
                UserAddressEditInfo editInfo= JsonUtil.convertJson2Model(jsonString, UserAddressEditInfo.class);
                if (editInfo!=null) {
                    int count = userAddressService.countByUserId(currentUserId);
                    UserAddress userAddress=editInfo.toUserAddress();
                    userAddress.setUserId(currentUserId);
                    userAddress.setIsDefault(count>0?0:1);
                    userAddress = userAddressService.save(userAddress);
                    return new ResponseData().put("new_address",userAddress);
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    @RequestMapping(value="/update_address")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData update_address(HttpServletRequest request,
                                       @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("edit_info");
            String jsonString=requestContext.getStringValue("edit_info");
            logger.info("update_address:{} ",jsonString!=null?jsonString:"");
            if (jsonString!=null) {
                UserAddressEditInfo editInfo= JsonUtil.convertJson2Model(jsonString, UserAddressEditInfo.class);
                if (editInfo!=null) {
                    if (editInfo.getAddressId()!=0) {
                        UserAddress userAddress = userAddressService.findOne(editInfo.getAddressId());
                        if (userAddress.getUserId()==currentUserId) {
                            UserAddress userAddressToSave=editInfo.toUserAddress();

                            userAddressToSave.setUserId(currentUserId);
                            userAddressToSave.setAddressId(userAddress.getAddressId());
                            userAddressToSave.setIsDefault(userAddress.getIsDefault());
                            userAddressToSave.setSort(userAddress.getSort());
                            userAddressToSave.setCreateTime(userAddress.getCreateTime());

                            userAddressToSave = userAddressService.save(userAddressToSave);
                            return new ResponseData().put("address",userAddressToSave);
                        }
                    }
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    @RequestMapping(value="/remove_address")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData remove_address(HttpServletRequest request,
                                       @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            HttpRequestContext requestContext = new HttpRequestContext(request, allRequestParams);
            requestContext.validateInputParams("ids");
            String idsString=requestContext.getStringValue("ids");

            if (idsString!=null&&idsString.length()>0) {
                List<Integer> ids = new ArrayList<>();
                StringTokenizer tokenizer = new StringTokenizer(idsString,"|");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken();
                    if (NumberValidationUtil.isWholeNumber(nextToken)) {
                        ids.add(Integer.valueOf(nextToken));
                    }
                }

                if (ids.size()>0) {
                    List<UserAddress> list = userAddressService.findAll(ids);
                    if (list!=null) {
                        List<UserAddress> listToSave = new ArrayList<>();
                        for (UserAddress userAddress : list) {
                            if (userAddress.getUserId()==currentUserId) {
                                userAddress.setDeleted(1);
                                listToSave.add(userAddress);
                            }
                        }
                        userAddressService.save(listToSave);
                    }
                    return new ResponseData();
                }
            }
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
        return new ErrorResponse(ErrorCode.ERROR_FAILED);
    }

    @RequestMapping(value="/list_address")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseData mmm_address(HttpServletRequest request,
                                    @RequestParam Map<String,String> allRequestParams) {
        int currentUserId = SecurityUtil.currentUserId();
        if (currentUserId==0) {
            return new ErrorResponse(ErrorCode.ERROR_ACCESS_DENIED);
        }
        try {
            List<UserAddress> list = userAddressService.findAddressByUserId(currentUserId);
            if (list==null) {
                list=new ArrayList<>();
            }
            return new ResponseData().put("list", list);
        } catch (ServerException e) {
            e.printStackTrace();
            return e.toResponseData();
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(ErrorCode.ERROR_SERVER_INNER);
        }
    }


}
