package com.bootdo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.bootdo.core.mybatis.dbid.SnowyDatabaseIdProvider;
import com.bootdo.core.mybatis.fieldfill.CustomMetaObjectHandler;
import com.bootdo.core.mybatis.permission.DataPermissionHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis 扩展插件配置
 *
 * @author caiyz
 * @since 2026-01-15 20:20
 */
@Configuration
public class MyBatisConfig {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataPermissionHandlerImpl()));
        return interceptor;
    }


    /**
     * 自定义公共字段自动注入
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    /**
     * 数据库 id选择器
     */
    @Bean
    public SnowyDatabaseIdProvider snowyDatabaseIdProvider() {
        return new SnowyDatabaseIdProvider();
    }

}
