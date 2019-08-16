package com.cpic.nlmis.service;

import com.cpic.nlmis.model.FeeDetailEntity;

import java.util.List;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/14 16:18
 */
public interface CpicFeeDetailService {
    int deleteByPrimaryKey(String id);

    int insert(FeeDetailEntity record);

    FeeDetailEntity selectByPrimaryKey(String id);

    List<FeeDetailEntity> selectAll();

    int updateByPrimaryKey(FeeDetailEntity record);


}
