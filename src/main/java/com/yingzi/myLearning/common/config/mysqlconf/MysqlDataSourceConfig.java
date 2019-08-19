package com.dfkj.myLearning.common.config.mysqlconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取edas mysql 的数据库配置信息
 * @author hk
 * @version V1.0
 * @DATE 2018/12/6 14:12
 */
@Component("mysqlDataSourceConfig")
@ConfigurationProperties(prefix = "edas.mysql")
public class MysqlDataSourceConfig {
    private String group;
    private String dataId;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
