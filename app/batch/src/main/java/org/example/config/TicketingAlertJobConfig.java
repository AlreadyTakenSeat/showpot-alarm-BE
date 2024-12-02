package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.listener.TicketingAlertJobListener;
import org.example.listener.TicketingAlertTriggerListener;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class TicketingAlertJobConfig {

    private final JobFactory jobFactory;

    @Bean
    @Primary
    public SchedulerFactoryBean alarmSchedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobFactory(jobFactory);
        schedulerFactory.setAutoStartup(true);
        return schedulerFactory;
    }

    @Bean
    public TicketingAlertJobListener ticketingAlertJobListener() {
        return new TicketingAlertJobListener();
    }

    @Bean
    public TicketingAlertTriggerListener ticketingAlertTriggerListener() {
        return new TicketingAlertTriggerListener();
    }

    @Bean
    public Scheduler ticketingAlertScheduler(
        SchedulerFactoryBean alarmSchedulerFactoryBean,
        JobListener ticketingAlertJobListener,
        TriggerListener ticketingAlertTriggerListener
    ) throws SchedulerException {
        Scheduler scheduler = alarmSchedulerFactoryBean.getScheduler();
        scheduler.getListenerManager().addJobListener(ticketingAlertJobListener);
        scheduler.getListenerManager().addTriggerListener(ticketingAlertTriggerListener);

        return scheduler;
    }
}
