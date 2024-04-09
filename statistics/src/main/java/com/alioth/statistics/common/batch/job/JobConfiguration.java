package com.alioth.statistics.common.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
public class JobConfiguration {

    @Bean(name = "batchJob")
    public Job batchJob(JobRepository jobRepository, Map<String, Step> stepMap) {
        return new JobBuilder("batchJob", jobRepository)
                .start(stepMap.get("simpleStep1"))
                .next(stepMap.get("stepMemberSales"))
                .next(stepMap.get("stepTeamSales"))
                .next(stepMap.get("stepHqSales"))
                .next(stepMap.get("stepRankProduct"))
                .next(stepMap.get("stepRankMember"))
                .build();
    }

    @Bean(name = "simpleStep1")
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet, platformTransactionManager).build();
    }

    @Bean(name = "testTasklet")
    public Tasklet testTasklet(){
        return ((contribution, chunkContext) -> {
            log.info(">>>>> This is Step1");
            return RepeatStatus.FINISHED;
        });
    }


}
