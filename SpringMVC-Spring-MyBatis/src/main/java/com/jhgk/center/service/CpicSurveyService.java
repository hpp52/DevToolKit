package com.dfkj.center.service;

import com.dfkj.center.model.SurveyEntity;

import java.util.List;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/9 9:07
 */
public interface dfkjSurveyService {
    int deleteByPrimaryKey(String id);

    int insert(SurveyEntity record);

    SurveyEntity selectByPrimaryKey(String id);

    List<SurveyEntity> getAll();

    int updateByPrimaryKey(SurveyEntity record);
}
