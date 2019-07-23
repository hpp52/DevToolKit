package com.yingzi.myLearning.common.config.mysqlconf;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO
 *
 * @author hk
 * @version V1.0
 * @DATE 2018/12/6 14:41
 */
@Getter
@Setter
public class DataSourceModel {
    private String validationQuery;
    private String driverClassName;
    private String jdbcUrl;
    private String jdbcUserName;
    private String jdbcUserPassword;
    private int initialSize;
    private int maxActive;
    private int minIdle;
    private int maxWait;
}
