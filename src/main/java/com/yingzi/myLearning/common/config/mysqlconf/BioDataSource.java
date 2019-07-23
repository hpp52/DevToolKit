package com.yingzi.myLearning.common.config.mysqlconf;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.edas.configcenter.config.ConfigService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * 数据源
 * @author hk
 * @version V1.0
 * @DATE 2018/12/6 14:27
 */
@Configuration("bioDataSource")
@EnableConfigurationProperties({MysqlDataSourceConfig.class})
@MapperScan("com.yingzi.center.bio.db.mapper")
public class BioDataSource extends DruidDataSource implements InitializingBean {
    private static final long serialVersionUID = -846717973236947625L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BioDataSource.class);
    @Autowired
    @Qualifier("mysqlDataSourceConfig") //需要指定，不指定，會找不到對應的文件
    private MysqlDataSourceConfig conf;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(conf.getDataId()) || StringUtils.isEmpty(conf.getGroup())) {
            return;
        }
        String configInfo = ConfigService.getConfig(conf.getDataId(), conf.getGroup(), 3000);
        LOGGER.info(configInfo);
        DataSourceModel ds = parseObject(configInfo, DataSourceModel.class);
        setValidationQuery(ds.getValidationQuery());
        setDbType(JdbcConstants.MYSQL);
        setDriverClassName(ds.getDriverClassName());
        setUrl(ds.getJdbcUrl());
        setUsername(ds.getJdbcUserName());
        setPassword(ds.getJdbcUserPassword());
        setInitialSize(ds.getInitialSize());
        setMaxActive(ds.getMaxActive());
        setMinIdle(ds.getMinIdle());
        setMaxWait(ds.getMaxWait());
    }

}
