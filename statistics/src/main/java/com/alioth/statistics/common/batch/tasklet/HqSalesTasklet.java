package com.alioth.statistics.common.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HqSalesTasklet {



    @Bean(name = "taskletHqSales")
    public Tasklet taskletHqSales(){
        return ((contribution, chunkContext) -> {
            log.info("============================================");
            log.info("===========This is TaskletHqSales===========");




            log.info("===========This is TaskletHqSales===========");
            log.info("============================================");
            return RepeatStatus.FINISHED;
        });
    }
}
