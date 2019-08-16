package com.cpic.nlmis.dao;

import com.cpic.nlmis.model.InjuredEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InjuredEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(InjuredEntity record);

    InjuredEntity selectByPrimaryKey(String id);

    List<InjuredEntity> selectAll();

    int updateByPrimaryKey(InjuredEntity record);
}