package com.cpic.nlmis.service;

import com.cpic.nlmis.dto.InjuredInfoDto;
import com.cpic.nlmis.model.InjuredEntity;

import java.util.List;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/14 16:19
 */
public interface CpicInjuredService {
    int deleteByPrimaryKey(String id);

    int insert(InjuredEntity record);

    InjuredEntity selectByPrimaryKey(String id);

    List<InjuredEntity> selectAll();

    int updateByPrimaryKey(InjuredEntity record);

    int submit(InjuredInfoDto dto) throws Exception;
}
