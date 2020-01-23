package com.ingeniance.batch.quartz;

import org.quartz.Trigger;

public class TriggerMonitor {
    private Trigger trigger;

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return trigger;
    }
}
