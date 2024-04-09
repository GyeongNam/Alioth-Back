package com.alioth.statistics.common.batch.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MemberSalesTasklet {

    @Bean(name = "taskletMemberSales")
    public Tasklet taskletMemberSales(){
        return ((contribution, chunkContext) -> {
            log.info(">>>>> This is taskletMemberSales");
            return RepeatStatus.FINISHED;
        });
    }
}
