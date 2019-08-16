package com.cpic.nlmis.model;

import lombok.Data;

import java.util.Date;
@Data
public class UserEntity {
    private String id;

    private String staffCode;

    private String staffName;

    private String passWord;

    private Date pwdEndTime;

    private Date lastestLoginTime;

    private Short isValid;

    private Short isLocked;

    private String ipAddress;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", staffCode=").append(staffCode);
        sb.append(", staffName=").append(staffName);
        sb.append(", passWord=").append(passWord);
        sb.append(", pwdEndTime=").append(pwdEndTime);
        sb.append(", lastestLoginTime=").append(lastestLoginTime);
        sb.append(", isValid=").append(isValid);
        sb.append(", isLocked=").append(isLocked);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append("]");
        return sb.toString();
    }
}