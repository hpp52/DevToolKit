package com.cpic.nlmis.service.impl;

import com.cpic.nlmis.dao.FeeDetailEntityMapper;
import com.cpic.nlmis.model.FeeDetailEntity;
import com.cpic.nlmis.service.CpicFeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/14 16:23
 */
@Service("cpicFeeDetailService")
public class CpicFeeDetailServiceImpl implements CpicFeeDetailService {
    @Autowired
    private FeeDetailEntityMapper feeDetailEntityMapper;
    @Override
    public int deleteByPrimaryKey(String id) {
        return feeDetailEntityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FeeDetailEntity record) {
        return feeDetailEntityMapper.insert(record);
    }

    @Override
    public FeeDetailEntity selectByPrimaryKey(String id) {
        return feeDetailEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FeeDetailEntity> selectAll() {
        return feeDetailEntityMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(FeeDetailEntity record) {
        return feeDetailEntityMapper.updateByPrimaryKey(record);
    }


}
