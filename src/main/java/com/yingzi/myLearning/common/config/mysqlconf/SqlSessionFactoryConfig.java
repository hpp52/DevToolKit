package com.yingzi.myLearning.common.config.mysqlconf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源连接池工厂
 * @author hk
 * @version V1.0
 * @DATE 2018/12/6 14:23
 */
@Configuration
public class SqlSessionFactoryConfig {

    /*分页拦截使用的，MP自带*/
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    /*分页拦截使用的，Pagehelper自带*/
    @Bean
    public PageInterceptor pageInterceptor(){
        return new PageInterceptor();
    }

    /******配置事务管理********/
   /* @Bean(name="drdsBioTransactionManager")
    public PlatformTransactionManager drdsTransactionManager(@Qualifier("bioDataSource") DataSource dataSource) {
        return new DrdsTransaction(dataSource);
    }*/

}
