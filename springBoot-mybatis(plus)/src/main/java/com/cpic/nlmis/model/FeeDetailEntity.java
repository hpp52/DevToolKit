package com.cpic.nlmis.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;
@Data
@ApiModel
public class FeeDetailEntity {
    private String id;

    private String injuryName;
    @ApiParam(value = "收费项目",required = true)
    @ApiModelProperty(value = "收费项目", example = "空气",required = true)
    private String item;

    private String itemName;

    @ApiParam(value = "规格",required = true)
    @ApiModelProperty(value = "规格", example = "0",required = true)
    private String spec;
    @ApiParam(value = "单价",required = true)
    @ApiModelProperty(value = "单价", example = "0",required = true)
    private String price;
    @ApiParam(value = "数量",required = true)
    @ApiModelProperty(value = "数量", example = "0",required = true)
    private String itemNumber;
    @ApiParam(value = "金额",required = true)
    @ApiModelProperty(value = "金额", example = "0",required = true)
    private String amount;
    @ApiParam(value = "自负金额",required = true)
    @ApiModelProperty(value = "自负金额", example = "0",required = true)
    private String selfAmount;

    private String injuredId;

    private Date actDate;

    private String status;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", injuryName=").append(injuryName);
        sb.append(", item=").append(item);
        sb.append(", itemName=").append(itemName);
        sb.append(", spec=").append(spec);
        sb.append(", price=").append(price);
        sb.append(", itemNumber=").append(itemNumber);
        sb.append(", amount=").append(amount);
        sb.append(", selfAmount=").append(selfAmount);
        sb.append(", injuredId=").append(injuredId);
        sb.append(", actDate=").append(actDate);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}