package com.cpic.nlmis.dao;

import com.cpic.nlmis.model.FeeDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeDetailEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(FeeDetailEntity record);

    FeeDetailEntity selectByPrimaryKey(String id);

    List<FeeDetailEntity> selectAll();

    int updateByPrimaryKey(FeeDetailEntity record);
}