package com.cpic.nlmis.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.util.Date;
@ApiModel
public class InjuredEntity {
    @ApiParam(value = "伤者信息记录id")
    @ApiModelProperty(value = "伤者信息记录id", example = "-11")
    private String id;

    @ApiParam(value = "伤者姓名",required = true)
    @ApiModelProperty(value = "伤者姓名", example = "拉拉张",required = true)
    private String injuryName;
    @ApiParam(value = "身份证号码")
    @ApiModelProperty(value = "身份证号码", example = "430556")
    private String idNo;
    @ApiParam(value = "性别")
    @ApiModelProperty(value = "性别", example = "女")
    private String sex;
    @ApiParam(value = "年龄")
    @ApiModelProperty(value = "年龄", example = "45")
    private String age;
    @ApiParam(value = "地址")
    @ApiModelProperty(value = "地址", example = "45")
    private String address;
    @ApiParam(value = "医院")
    @ApiModelProperty(value = "医院", example = "某某医院")
    private String hospital;
    @ApiParam(value = "医院代码")
    @ApiModelProperty(value = "医院代码", example = "430001")
    private String hospitalCode;
    @ApiParam(value = "症状描述")
    @ApiModelProperty(value = "症状描述", example = "头大")
    private String diagnosis;
    @ApiParam(value = "已废弃字段 非必填")
    @ApiModelProperty(value = "已废弃字段 非必填")
    private String feeDetailId;
    @ApiParam(value = "科室")
    @ApiModelProperty(value = "科室")
    private String section;
    @ApiParam(value = "床位")
    @ApiModelProperty(value = "床位")
    private String bed;
    @ApiParam(value = "客户id")
    @ApiModelProperty(value = "客户id")
    private String customerId;
    @ApiParam(value = "签名")
    @ApiModelProperty(value = "签名")
    private String signature;

    private Date actDate;

    private String status;
    @ApiParam(value = "集团")
    @ApiModelProperty(value = "集团")
    private String hospitalGroup;

    private String ipAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getInjuryName() {
        return injuryName;
    }

    public void setInjuryName(String injuryName) {
        this.injuryName = injuryName == null ? null : injuryName.trim();
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital == null ? null : hospital.trim();
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode == null ? null : hospitalCode.trim();
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis == null ? null : diagnosis.trim();
    }

    public String getFeeDetailId() {
        return feeDetailId;
    }

    public void setFeeDetailId(String feeDetailId) {
        this.feeDetailId = feeDetailId == null ? null : feeDetailId.trim();
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section == null ? null : section.trim();
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed == null ? null : bed.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public Date getActDate() {
        return actDate;
    }

    public void setActDate(Date actDate) {
        this.actDate = actDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getHospitalGroup() {
        return hospitalGroup;
    }

    public void setHospitalGroup(String hospitalGroup) {
        this.hospitalGroup = hospitalGroup == null ? null : hospitalGroup.trim();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", injuryName=").append(injuryName);
        sb.append(", idNo=").append(idNo);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", address=").append(address);
        sb.append(", hospital=").append(hospital);
        sb.append(", hospitalCode=").append(hospitalCode);
        sb.append(", diagnosis=").append(diagnosis);
        sb.append(", feeDetailId=").append(feeDetailId);
        sb.append(", section=").append(section);
        sb.append(", bed=").append(bed);
        sb.append(", customerId=").append(customerId);
        sb.append(", signature=").append(signature);
        sb.append(", actDate=").append(actDate);
        sb.append(", status=").append(status);
        sb.append(", hospitalGroup=").append(hospitalGroup);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append("]");
        return sb.toString();
    }
}