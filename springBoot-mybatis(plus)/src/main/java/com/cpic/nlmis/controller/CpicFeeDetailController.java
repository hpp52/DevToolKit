package com.cpic.nlmis.controller;

import com.cpic.nlmis.common.rest.RestResponse;
import com.cpic.nlmis.dto.InjuredInfoDto;
import com.cpic.nlmis.model.FeeDetailEntity;
import com.cpic.nlmis.service.CpicFeeDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/16 8:47
 */
@RestController
@Api(tags = {"费用明细接口"})
@RequestMapping("/api/Y00043202/feeDetailServcie")
public class CpicFeeDetailController {
    @Autowired
    private CpicFeeDetailService cpicFeeDetailService;

    @ApiOperation(value = "费用明细上传")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RestResponse submit(FeeDetailEntity params)  {
        return  RestResponse.builder(cpicFeeDetailService.insert(params));
    }

}
