package com.study.springbatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration

// JPA 리포지토리 활성화
@EnableJpaRepositories(

        // 스캔 리포지터리 설정, 사용할 엔티티 매니저 및 트랜젝션 매니저 설정
        basePackages = "com.study.springbatch.repository",
        entityManagerFactoryRef = "dataEntityManager",
        transactionManagerRef = "dataTransactionManager"
)
public class DataDBConfig {

    @Bean

    // yml(공통 접두사 spring.datasource-data) 변수 설정 값 불러오기
    @ConfigurationProperties(prefix = "spring.datasource-data")
    public DataSource dataDBSource() {

        // spring.datasource-data 변수의 DB 설정 값들을 불러 DataSource 생성
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean dataEntityManager() {

        // EntityManagerFactory 생성하는 Spring Bean 생성
        LocalContainerEntityManagerFactoryBean entityManager
                = new LocalContainerEntityManagerFactoryBean();

        // 데이터 소스 설정
        entityManager.setDataSource(dataDBSource());

        // JPA 엔티티 클래스들이 위치한 패키지를 지정
        entityManager.setPackagesToScan(new String[]{"com.study.springbatch.entity"});

        // JPA 구현체의 벤더 어댑터를 설정 (Hibernate 사용)
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Hibernate 설정을 위한 맵 생성
        HashMap<String, Object> properties = new HashMap<>();

        // 애플리케이션 실행 시점 DB 스키마 설정, 실행된 SQL 쿼리를 콘솔에 출력 설정
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");

        // 설정 프로퍼티를 EntityManagerFactory에 설정
        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean
    public PlatformTransactionManager dataTransactionManager() {

        // JPA 트랜젝션 매니저를 생성
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        // 트랜젝션 매니저에 EntityManagerFactory 설정
        transactionManager.setEntityManagerFactory(dataEntityManager().getObject());

        return transactionManager;
    }
}