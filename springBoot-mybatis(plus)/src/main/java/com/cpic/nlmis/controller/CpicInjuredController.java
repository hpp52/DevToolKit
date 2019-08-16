package com.cpic.nlmis.controller;

import com.cpic.nlmis.common.rest.RestResponse;
import com.cpic.nlmis.common.utils.SnowflakeIdWorker;
import com.cpic.nlmis.dto.InjuredInfoDto;
import com.cpic.nlmis.service.CpicInjuredService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.cpic.nlmis.common.utils.DecryptUtil.decryptBASE64AndRemoveSalt;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/14 16:29
 */
@RestController
@Api(tags = {"伤者信息上传接口"})
@RequestMapping("/api/Y00043202/theInjuredInfoServcie")
public class CpicInjuredController {
    @Autowired
    private CpicInjuredService cpicInjuredService;

    @ApiOperation(value = "伤者信息上传")
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public RestResponse submit(InjuredInfoDto params) throws Exception {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long id = idWorker.nextId();
        //解密操作
        InjuredInfoDto reuslt=new InjuredInfoDto();
        reuslt.setFeeDetail(params.getFeeDetail());
        reuslt.setId(id.toString());
        reuslt.setInjuryName(decryptBASE64AndRemoveSalt(params.getInjuryName()));
        reuslt.setIdNo(decryptBASE64AndRemoveSalt(params.getIdNo()));
        reuslt.setSex(decryptBASE64AndRemoveSalt(params.getSex()));
        reuslt.setAge(decryptBASE64AndRemoveSalt(params.getAge()));
        reuslt.setAddress(decryptBASE64AndRemoveSalt(params.getAddress()));
        reuslt.setHospital(decryptBASE64AndRemoveSalt(params.getHospital()));
        reuslt.setHospitalCode(decryptBASE64AndRemoveSalt(params.getHospitalCode()));
        reuslt.setDiagnosis(decryptBASE64AndRemoveSalt(params.getDiagnosis()));
        reuslt.setFeeDetailId(params.getFeeDetailId());
        reuslt.setSection(decryptBASE64AndRemoveSalt(params.getSection()));
        reuslt.setBed(decryptBASE64AndRemoveSalt(params.getBed()));
        reuslt.setCustomerId(decryptBASE64AndRemoveSalt(params.getCustomerId()));
        reuslt.setSignature(params.getSignature());
        reuslt.setActDate(new Date());
        reuslt.setStatus("1");
        reuslt.setHospitalGroup(params.getHospitalGroup());
        reuslt.setIpAddress("");

       return  RestResponse.builder(cpicInjuredService.submit(params));
    }

    @ApiOperation(value = "伤者信息List")
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    public  RestResponse list(){
        return  RestResponse.builder(cpicInjuredService.selectAll());
    }



}
