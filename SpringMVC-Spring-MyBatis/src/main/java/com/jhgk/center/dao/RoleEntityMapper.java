package com.dfkj.center.dao;

import com.dfkj.center.model.RoleEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleEntity record);

    RoleEntity selectByPrimaryKey(String id);

    List<RoleEntity> selectAll();

    int updateByPrimaryKey(RoleEntity record);
}