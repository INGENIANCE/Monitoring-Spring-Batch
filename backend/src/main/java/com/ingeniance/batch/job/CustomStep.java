package com.ingeniance.batch.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomStep implements Tasklet, StepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(CustomStep.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("Custom step initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) throws Exception {
        try {
            // Add your business logic here.
            logger.info("Custom Step is running ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("Custom step ended.");
        return ExitStatus.COMPLETED;
    }
}
