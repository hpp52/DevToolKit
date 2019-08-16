package com.cpic.nlmis.dao;

import com.cpic.nlmis.model.UserEntity;
import java.util.List;

public interface UserEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserEntity record);

    UserEntity selectByPrimaryKey(String id);

    List<UserEntity> selectAll();

    int updateByPrimaryKey(UserEntity record);
}