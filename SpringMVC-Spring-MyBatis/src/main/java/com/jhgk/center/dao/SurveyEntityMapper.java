package com.dfkj.center.dao;

import com.dfkj.center.model.SurveyEntity;
import java.util.List;

public interface  SurveyEntityMapper {
    int deleteByPrimaryKey(String id);

    int insert(SurveyEntity record);

    SurveyEntity selectByPrimaryKey(String id);

    List<SurveyEntity> selectAll();

    int updateByPrimaryKey(SurveyEntity record);
}