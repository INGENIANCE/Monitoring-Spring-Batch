package com.ingeniance.batch.config;

import com.ingeniance.batch.job.CustomStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ingeniance.batch.job.BatchConstant.JOB_NAME;

@Configuration
public class JobConfig {
    @Bean
    protected Step customStep(StepBuilderFactory stepBuilders) {
        return stepBuilders
                .get("customStep")
                .tasklet(new CustomStep())
                .build();
    }

    @Bean
    public Job customJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders
                .get(JOB_NAME)
                .start(customStep(stepBuilders))
                .build();
    }
}
