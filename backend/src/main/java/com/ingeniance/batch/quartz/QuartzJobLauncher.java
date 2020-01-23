package com.ingeniance.batch.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJobLauncher extends QuartzJobBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JobLauncher jobLauncher;
    private JobLocator jobLocator;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            jobLocator = applicationContext.getBean(JobLocator.class);
            jobLauncher = applicationContext.getBean(JobLauncher.class);

            Map<String, Object> jobDataMap = context.getMergedJobDataMap();
            String jobName = (String) jobDataMap.get("jobName");
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            Job job = jobLocator.getJob(jobName);
            JobExecution jobExecution = jobLauncher.run(job, params);

            log.info("########### Status: " + jobExecution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

