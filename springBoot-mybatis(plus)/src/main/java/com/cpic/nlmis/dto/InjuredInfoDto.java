package com.cpic.nlmis.dto;

import com.cpic.nlmis.model.InjuredEntity;
import io.swagger.annotations.ApiParam;


/**
 * @Author: hk
 * @Description: 伤者信息submitDto
 * @Date: 2019/8/14 16:41
 */

public class InjuredInfoDto extends InjuredEntity {
    /**
     * 内为json数组，数组字段为item,itemName,spec,price,number,amount,selfAmount
     */
    @ApiParam(value="费用明细 此处为json数组 必传字段", required = true)
    private  String feeDetail;

    public String getFeeDetail() {
        return feeDetail;
    }

    public void setFeeDetail(String feeDetail) {
        this.feeDetail = feeDetail;
    }
}
