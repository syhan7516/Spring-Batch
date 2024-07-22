package com.study.springbatch.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${JASYPT_KEY}")
    String KEY;

    // 사용할 암호화 알고리즘 정의
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {

        // 문자열 암호화를 수행하는 암호화기 생성
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

        // 암호화에 필요한 다양한 설정을 담는 인스턴스 생성
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        // 암호화 키(비밀번호) 설정
        config.setPassword(KEY);

        // 암호화에 사용할 알고리즘 설정
        config.setAlgorithm(ALGORITHM);

        // 키 획득 반복 횟수 설정
        config.setKeyObtentionIterations("1000");

        // 암호화기 풀의 크기 설정
        config.setPoolSize("1");

        // 보안 제공자 이름 설정
        config.setProviderName("SunJCE");

        // 솔트 생성기 클래스 설정
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");

        // 암호화된 문자열의 출력 타입을 Base64로 설정
        config.setStringOutputType("base64");

        // 설정을 암호화기에 적용
        encryptor.setConfig(config);

        // 설정된 암호화기를 반환
        return encryptor;
    }
}