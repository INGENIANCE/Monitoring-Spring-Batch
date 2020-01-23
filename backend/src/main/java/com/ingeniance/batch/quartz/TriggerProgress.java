package com.ingeniance.batch.quartz;

import java.util.Date;

public class TriggerProgress {

    private Date finalFireTime;

    private Date nextFireTime;

    private Date previousFireTime;

    private String jobKey;

    private String jobClass;

    private String step;

    private int percentage;

    public Date getFinalFireTime() {
        return finalFireTime;
    }

    public String getJobClass() {
        return jobClass;
    }

    public String getJobKey() {
        return jobKey;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public String getStep() { return step; }

    public int getPercentage() { return percentage; }

    public void setFinalFireTime(Date finalFireTime) {
        this.finalFireTime = finalFireTime;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public void setStep(String step) { this.step = step; }

    public void setPercentage(int percentage) { this.percentage = percentage; }
}
