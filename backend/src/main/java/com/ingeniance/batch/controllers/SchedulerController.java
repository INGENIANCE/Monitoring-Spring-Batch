package com.ingeniance.batch.controllers;

import com.ingeniance.batch.models.Config;
import com.ingeniance.batch.quartz.SchedulerStates;
import com.ingeniance.batch.quartz.TriggerMonitor;
import com.ingeniance.batch.quartz.TriggerProgress;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/scheduler")
@ConditionalOnExpression("'${scheduler.enabled}'=='true'")
public class SchedulerController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TriggerMonitor triggerMonitor;

    @Resource
    private Scheduler scheduler;

    @GetMapping("/pause")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pause() throws SchedulerException {
        log.info("SCHEDULER -> PAUSE COMMAND");
        scheduler.standby();
    }

    @GetMapping("/resume")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resume() throws SchedulerException {
        log.info("SCHEDULER -> RESUME COMMAND");
        scheduler.start();
    }

    @GetMapping("/run")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void run() throws SchedulerException {
        log.info("SCHEDULER -> START COMMAND");
        scheduler.start();
    }

    @GetMapping("/stop")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stop() throws SchedulerException {
        log.info("SCHEDULER -> STOP COMMAND");
        scheduler.shutdown(true);
    }

    @GetMapping(produces = "application/json")
    public Map<String, String> getStatus() throws SchedulerException {
        log.trace("SCHEDULER -> GET STATUS");
        String schedulerState = "";
        if (scheduler.isShutdown() || !scheduler.isStarted())
            schedulerState = SchedulerStates.STOPPED.toString();
        else if (scheduler.isStarted() && scheduler.isInStandbyMode())
            schedulerState = SchedulerStates.PAUSED.toString();
        else
            schedulerState = SchedulerStates.RUNNING.toString();
        return Collections.singletonMap("data", schedulerState.toLowerCase());
    }

    @GetMapping("/progress")
    public TriggerProgress getProgressInfo() throws SchedulerException {
        log.trace("SCHEDULER -> GET PROGRESS INFO");
        TriggerProgress progress = new TriggerProgress();

        CronTrigger jobTrigger = (CronTrigger) scheduler.getTrigger(triggerMonitor.getTrigger().getKey());
        if (jobTrigger != null && jobTrigger.getJobKey() != null) {
            progress.setJobKey(jobTrigger.getJobKey().getName());
            progress.setJobClass(jobTrigger.getClass().getSimpleName());
            progress.setFinalFireTime(jobTrigger.getFinalFireTime());
            progress.setNextFireTime(jobTrigger.getNextFireTime ());
            progress.setPreviousFireTime(jobTrigger.getPreviousFireTime());
        }

        return progress;
    }

    @GetMapping("/config")
    public ResponseEntity getConfig() {
        log.debug("SCHEDULER -> GET CRON EXPRESSION");
        CronTrigger trigger = (CronTrigger) triggerMonitor.getTrigger();
        return new ResponseEntity<>(Collections.singletonMap("data", trigger.getCronExpression()), HttpStatus.OK);
    }

    @PostMapping("/config")
    public ResponseEntity postConfig(@RequestBody Config config) throws SchedulerException {
        log.info("SCHEDULER -> NEW CRON EXPRESSION: {}", config.getCronExpression());
        CronTrigger trigger = (CronTrigger) triggerMonitor.getTrigger();

        TriggerBuilder<CronTrigger> triggerBuilder = trigger.getTriggerBuilder();
        CronTrigger newTrigger = triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(config.getCronExpression())).build();

        scheduler.rescheduleJob(triggerMonitor.getTrigger().getKey(), newTrigger);
        triggerMonitor.setTrigger(newTrigger);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
