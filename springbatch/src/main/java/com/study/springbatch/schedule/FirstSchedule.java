package com.study.springbatch.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class FirstSchedule {

    // JobLauncher, JobRegistry 필드 의존성 주입
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    // Schedule 등록
    // @Scheduled(cron = "10 * * * * *", zone = "Asia/Seoul")
    public void firstBatch() throws Exception {

        System.out.println("first schedule batch start");

        // 날짜 형식 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        // 현재 날짜를 설정한 형식으로 맞춘 뒤 문자열로 반환
        String date = dateFormat.format(new Date());

        // JobParameters 생성
        JobParameters jobParameters = new JobParametersBuilder()

                // parameter 추가
                .addString("date", date)

                // 설정한 parameter 모아 JobParameters 생성
                .toJobParameters();

        // 작업 실행 (배치 처리 작업 이름으로 등록된 작업 가져오기)
        jobLauncher.run(jobRegistry.getJob("firstJob"),jobParameters);
    }
}