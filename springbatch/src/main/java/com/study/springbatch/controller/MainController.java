package com.study.springbatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    // JobLauncher, JobRegistry 필드 의존성 주입
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/first")
    public String firstBatch(@RequestParam("value") String value) throws Exception {

        System.out.println("first controller api batch start");

        // JobParameters 생성
        JobParameters jobParameters = new JobParametersBuilder()

                // parameter 추가
                .addString("date", value)

                // 설정한 parameter 모아 JobParameters 생성
                .toJobParameters();

        // 작업 실행 (배치 처리 작업 이름으로 등록된 작업 가져오기)
        jobLauncher.run(jobRegistry.getJob("firstJob"),jobParameters);

        return "Batch Success";
    }
}