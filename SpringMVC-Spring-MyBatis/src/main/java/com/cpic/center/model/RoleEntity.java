package com.cpic.center.model;

import lombok.Data;

@Data
public class RoleEntity {
    private String id;

    private String roleName;

    private String description;

    private String roleType;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleName=").append(roleName);
        sb.append(", description=").append(description);
        sb.append(", roleType=").append(roleType);
        sb.append("]");
        return sb.toString();
    }
}