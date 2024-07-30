package com.study.springbatch.bacth;

import com.study.springbatch.entity.AfterEntity;
import com.study.springbatch.entity.BeforeEntity;
import com.study.springbatch.repository.AfterRepository;
import com.study.springbatch.repository.BeforeRepository;
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

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FirstBatch {

    // 배치 작업에 필요한 레포지터리 및 트랜젝션 의존성 주입
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final BeforeRepository beforeRepository;
    private final AfterRepository afterRepository;

    // 작업 정의
    @Bean
    public Job firstJob() {

        System.out.println("기본 테이블의 데이터를 다른 테이블에 넣기 작업 : 시작");

        // 작업 이름, 작업에 대한 추적 데이터 저장 레포지터리 설정
        return new JobBuilder("firstJob", jobRepository)

                // 시작 Step 정의
                .start(firstStep())
                .build();
    }

    // 단계 정의
    @Bean
    public Step firstStep() {

        System.out.println("기본 테이블의 데이터를 다른 테이블에 넣기 작업 : Step");

        // 단계 이름, 단계에 대한 추적 데이터 저장 레포지터리 설정
        return new StepBuilder("firstStep",jobRepository)

                // 읽을 데이터 타입, 쓰기를 진행할 데이터 타입 설정
                // 청크 크기 설정 (처리 개수, 관리 트랜젝션)
                .<BeforeEntity, AfterEntity> chunk(10,platformTransactionManager)

                // 읽기, 처리, 쓰기 설정
                .reader(beforeReader())
                .processor(middleProcessor())
                .writer(afterWriter())
                .build();
    }

    // 읽기 정의
    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader() {

        // 읽기 설정
        return new RepositoryItemReaderBuilder<BeforeEntity>()

                // 읽기 이름 정의
                .name("beforeReader")

                // 끊어 읽을 크기
                .pageSize(10)

                // 수행하려는 JPA 쿼리
                .methodName("findAll")

                // JPA 쿼리 대상 레포지터리
                .repository(beforeRepository)

                // 페이지 단위 처리 과정에서 순서가 맞게 하기 위해 정렬
                .sorts(Map.of("userId", Sort.Direction.ASC))
                .build();
    }

    // 처리 정의
    @Bean
    public ItemProcessor<BeforeEntity,AfterEntity> middleProcessor() {

        // 처리 설정
        return new ItemProcessor<BeforeEntity, AfterEntity>() {

            @Override
            public AfterEntity process(BeforeEntity item) throws Exception {

                return AfterEntity.builder()
                        .userId(item.getUserId())
                        .userName(item.getUserName())
                        .build();
            }
        };
    }

    // 쓰기 정의
    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter() {

        // 쓰기 설정
        return new RepositoryItemWriterBuilder<AfterEntity>()

                // 쓰기를 수행하려는 레포지터리
                .repository(afterRepository)

                // 수행하려는 JPA 쿼리
                .methodName("save")
                .build();
    }
}