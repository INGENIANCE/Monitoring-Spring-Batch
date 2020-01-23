package com.ingeniance.batch.config;

import com.ingeniance.batch.quartz.AutowiringSpringBeanJobFactory;
import com.ingeniance.batch.quartz.QuartzJobLauncher;
import com.ingeniance.batch.quartz.TriggerMonitor;
import org.quartz.spi.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.Map;

import static com.ingeniance.batch.job.BatchConstant.JOB_NAME;

@Configuration
@ConditionalOnExpression("'${scheduler.enabled}'=='true'")
public class QuartzConfig {
    @Value("${scheduler.trigger.cron-expression}")
    private String cronExpression;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(QuartzJobLauncher.class);
        Map<String, Object> map = new HashMap<>();
        map.put("jobName", JOB_NAME);
        jobDetailFactoryBean.setJobDataAsMap(map);
        return jobDetailFactoryBean;
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
        cronTriggerFactoryBean.setCronExpression(this.cronExpression);
        return cronTriggerFactoryBean;
    }

    @Bean
    public TriggerMonitor triggerMonitor() {
        TriggerMonitor triggerMonitor = new TriggerMonitor();
        triggerMonitor.setTrigger(cronTriggerFactoryBean().getObject());
        return triggerMonitor;
    }

    @Bean
    public JobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setJobFactory(jobFactory);
        scheduler.setTriggers(cronTriggerFactoryBean().getObject());
        scheduler.setApplicationContext(applicationContext);
        return scheduler;
    }
}
