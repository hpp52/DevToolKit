package com.dfkj.center.service.impl;

import com.dfkj.center.dao.SurveyEntityMapper;
import com.dfkj.center.model.SurveyEntity;
import com.dfkj.center.service.dfkjSurveyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/9 9:08
 */
@Service("dfkjSurveyService")
public class dfkjSurveyServiceImpl implements dfkjSurveyService {
    private static final Logger logger = Logger.getLogger(dfkjSurveyService.class);
    @Autowired
    private SurveyEntityMapper surveyEntityMapper;
    @Override
    public int deleteByPrimaryKey(String id) {
        return surveyEntityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SurveyEntity record) {
        return surveyEntityMapper.insert(record);
    }

    @Override
    public SurveyEntity selectByPrimaryKey(String id) {
        return surveyEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SurveyEntity> getAll() {
        return surveyEntityMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(SurveyEntity record) {
        return surveyEntityMapper.updateByPrimaryKey(record);
    }
}
