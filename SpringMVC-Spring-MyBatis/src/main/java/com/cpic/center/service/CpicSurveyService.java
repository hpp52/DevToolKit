package com.cpic.center.service;

import com.cpic.center.model.SurveyEntity;

import java.util.List;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/9 9:07
 */
public interface CpicSurveyService {
    int deleteByPrimaryKey(String id);

    int insert(SurveyEntity record);

    SurveyEntity selectByPrimaryKey(String id);

    List<SurveyEntity> getAll();

    int updateByPrimaryKey(SurveyEntity record);
}
