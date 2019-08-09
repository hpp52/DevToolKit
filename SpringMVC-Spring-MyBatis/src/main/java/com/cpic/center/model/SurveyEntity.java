package com.cpic.center.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class SurveyEntity {
    private String id;

    private String usrName;

    private String departmentName;

    private Integer questionCode;

    private Integer replyCode;

    private String replyDetail;

    private Date finishDate;

    private Date actDate;

    private Integer isValid;

    private BigDecimal newColumn;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", usrName=").append(usrName);
        sb.append(", departmentName=").append(departmentName);
        sb.append(", questionCode=").append(questionCode);
        sb.append(", replyCode=").append(replyCode);
        sb.append(", replyDetail=").append(replyDetail);
        sb.append(", finishDate=").append(finishDate);
        sb.append(", actDate=").append(actDate);
        sb.append(", isValid=").append(isValid);
        sb.append(", newColumn=").append(newColumn);
        sb.append("]");
        return sb.toString();
    }
}