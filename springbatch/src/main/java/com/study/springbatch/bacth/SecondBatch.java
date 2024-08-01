package com.study.springbatch.bacth;

import com.study.springbatch.entity.WinEntity;
import com.study.springbatch.repository.WinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SecondBatch {

    // 배치 작업에 필요한 레포지터리 및 트랜젝션 의존성 주입
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    
    private final WinRepository winRepository;
    
    // 작업 정의
    @Bean
    public Job JobSecondJob() {

        System.out.println("테이블의 데이터를 변경 후 테이블에 저장하는 작업 : 시작");

        // 작업 이름, 작업에 대한 추적 데이터 저장 레포지터리 설정
        return new JobBuilder("secondJob",jobRepository)

                // 시작 Step 정의
                .start(secondStep())
                .build();
    }

    // 단계 정의
    @Bean
    public Step secondStep() {

        System.out.println("테이블의 데이터를 변경 후 테이블에 저장하는 작업 : Step");

        // 단계 이름, 단계에 대한 추적 데이터 저장 레포지터리 설정
        return new StepBuilder("secondStep",jobRepository)

                // 읽을 데이터 타입, 쓰기를 진행할 데이터 타입 설정
                // 청크 크기 설정 (처리 개수, 관리 트랜젝션)
                .<WinEntity, WinEntity> chunk(10,platformTransactionManager)

                // 읽기, 처리, 쓰기 설정
                .reader(winReader())
                .processor(trueProcessor())
                .writer(winWriter())
                .build();
    }

    // 읽기 정의
    @Bean
    public RepositoryItemReader<WinEntity> winReader() {

        // 읽기 설정
        return new RepositoryItemReaderBuilder<WinEntity>()

                // 읽기 이름 정의
                .name("winReader")

                // 끊어 읽을 크기
                .pageSize(10)

                // 수행하려는 JPA 쿼리
                .methodName("findByWinGreaterThanEqual")

                // 조건에 들어 인자 설정
                .arguments(Collections.singletonList(10L))

                // JPA 쿼리 대상 레포지터리
                .repository(winRepository)

                // 페이지 단위 처리 과정에서 순서가 맞게 하기 위해 정렬
                .sorts(Map.of("winId", Sort.Direction.ASC))
                .build();
    }

    // 처리 정의
    @Bean
    public ItemProcessor<WinEntity, WinEntity> trueProcessor() {

        // 처리 설정
        return item -> {
            item.changeReward();
            return item;
        };
    }

    // 쓰기 정의
    @Bean
    public RepositoryItemWriter<WinEntity> winWriter() {

        // 쓰기 설정
        return new RepositoryItemWriterBuilder<WinEntity>()

                // 쓰기를 수행하려는 레포지터리
                .repository(winRepository)

                // 수행하려는 JPA 쿼리
                .methodName("save")
                .build();
    }
}
