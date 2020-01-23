package com.ingeniance.batch.models;

public class Config {
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    private String cronExpression;
}
