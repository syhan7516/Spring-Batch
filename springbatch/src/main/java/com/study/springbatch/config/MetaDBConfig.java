package com.study.springbatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MetaDBConfig {

    // 다중 연결에서 충돌 방지를 위해 해당 빈을 기본으로 설정
    @Primary
    @Bean
    
    // yml(공통 접두사 spring.datasource-meta) 변수 설정 값 불러오기
    @ConfigurationProperties(prefix = "spring.datasource-meta")
    public DataSource metaDBSource() {

        // spring.datasource-meta 변수의 DB 설정 값들을 불러 DataSource 생성
        return DataSourceBuilder.create().build();
    }

    // 다중 연결에서 충돌 방지를 위해 해당 빈을 기본으로 설정
    @Primary
    @Bean

    // 데이터 소스에 대한 트랜잭션 매니저 정의
    public PlatformTransactionManager metaTransactionManager() {

        // DataSource 객체를 받아 트랜젝션 매니저 생성
        return new DataSourceTransactionManager(metaDBSource());
    }
}