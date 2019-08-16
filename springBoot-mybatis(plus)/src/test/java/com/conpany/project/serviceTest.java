package com.conpany.project;
import java.util.Date;

import com.cpic.nlmis.CpicApp;
import com.cpic.nlmis.model.FeeDetailEntity;
import com.cpic.nlmis.service.CpicFeeDetailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/16 10:34
 */
@SpringBootTest(classes = { CpicApp.class, serviceTest.class })
public class serviceTest extends Tester {
    @Autowired
    private CpicFeeDetailService cpicFeeDetailService;

    @Test
    public  void  CRUDTest(){
        FeeDetailEntity params=new FeeDetailEntity();
        params.setId("-54165");
        params.setInjuryName("54");
        params.setItem("545");
        params.setItemName("12");
        params.setSpec("564");
        params.setPrice("45654");
        params.setItemNumber("546");
        params.setAmount("6");
        params.setSelfAmount("564");
        params.setInjuredId("546");
        params.setActDate(new Date());
        params.setStatus("1");

        cpicFeeDetailService.insert(params);
    }

}
